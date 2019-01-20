# Hello Airflow
![guide](https://aldente0630.github.io/data-engineering/2018/06/17/developing-workflows-with-apache-airflow.html)

## 설치
```bash
//SLUGIFY_USES_TEXT_UNIDECODE=yes 이걸 해줘야 설치가 되서 넣어줬다.
sudo SLUGIFY_USES_TEXT_UNIDECODE=yes pip3 install apache-airflow

//Airflow SQLite 데이터베이스를 만들고 초기화하는 명령을 실행
airflow initdb

// db 초기화 이후에 version을 확인해서 초기 설정이 잘된건지 확인
airflow version

// airflow 가 설치되었으니, 웹콘솔을 띄우려면
airflow webserver - p 8989  // http://localhost:8989 로 접근, 포트는 마음대로

// airflow 스케줄러 시작시키기
airflow scheduler
```

## AirFlow를 언제쓰나?
 복잡한 계산을 요하는 작업흐름과 데이터 처리 파이프라인을 조율하기 위해 만든 오픈소스 도구이다. 길이가 긴 스크립트 실행을 cron으로 돌리거나 빅데이터 처리 배치 작업을 정기적으로 수행하려고 할 때 Airflow가 도움이 될 수 있다.

## 작업 흐름
Airflow 상의 작업흐름은 방향성 비순환 그래프(DAG)로 설계된다. 즉, 작업흐름을 짤 때 그것이 어떻게 독립적으로 실행가능한 태스크들로 나뉠 수 있을까 생각해봐야한다. 그 다음에야 각 태스크를 그래프로 결합하여 전체적인 논리 흐름에 맞게 합칠 수 있다.
![DAG](https://github.com/cacacoo/stack/blob/master/resource/images/airflow_1_DAG.png)
그래프 모양이 작업흐름의 전반적인 논리 구조를 결정한다. Airflow DAG는 여러 분기를 포함할 수 있고 작업흐름 실행 시 건너뛸 지점과 중단할 지점을 결정할 수 있다.

## Operator and Task
오퍼레이터는 DAG 내 실행 함수들을 의미하며, Task는 실행 함수의 실제 동작하는 인스턴스다. 객체지향에 대비하자면, 오퍼레이터는 class, Task는 object 이다.
위 파이프라인을 예를 들면, run_this_first -> branching -> branch_a-> follow_branch_a -> join 이 하나의 DAG이고, 각각이 오퍼레이터가 된다.

## Hello world!
내 경우, airflow 폴더가 Home 에 생성되었다.
들어가서 내 dag를 둘 폴더를 하나 만들고, 거기에 예제를 위한 hello_airflow.py 를 만들었다.
```python
from datetime import datetime
from airflow import DAG
from airflow.operators.dummy_operator import DummyOperator
from airflow.operators.python_operator import PythonOperator
from airflow.operators import MySuperOperator

def print_hello():
    return "Hello airflow!"

dag = DAG('hello_airflow', description="simple tutorial DAG",
         schedule_interval='*/3 * * * *',
         start_date= datetime(2019, 1, 20), catchup=False)

dummy_operator = DummyOperator(task_id='dummy_task', retries=3, dag=dag)

hello_operator = PythonOperator(task_id='hello_task', python_callable=print_hello, dag=dag)

my_super_operator = MySuperOperator(operator_param='laser beam!',
                                   task_id='special_attack', dag=dag)

dummy_operator >> hello_operator >> my_super_operator
```

```
참고
쥬피터 노트북으로 작업하면 ipynb 파일로 떨어져서, 읽지를 못한다.
py파일로 만들어주고, airflow scheduler 를 실행하면 끝
```


### 코드 분석
```python
dag = DAG('hello_airflow', description="simple tutorial DAG",
         schedule_interval='0 12 * * *',
         start_date= datetime(2019, 1, 20), catchup=False)
```
파라미터 순서로
1: DAG 이름설정
2: DAG 설명
3.리눅스/유닉스 크론표현식,  분[0-59 or 시작시간/단위] 시[0-23] 일[1-31 /?] 월[1-12] 요일[0-6 /?]
https://gs.saro.me/dev?page=3&tn=548
4: DAG 시작일자

```python
dummy_operator = DummyOperator(task_id='dummy_task', retries=3, dag=dag)

hello_operator = PythonOperator(task_id='hello_task', python_callable=print_hello, dag=dag)

dummy_operator >> hello_operator
```
더미 오퍼레이터 - 아무일도 하지 않는 오퍼레이터
1: task 명
2: 실패 시 반복횟수
3: task를 등록할 dag 주소

파이썬 오퍼레이터 - 파이썬 함수 호출용 오퍼레이터
1: task명
2: 파이썬 함수
3: task 등록할 dag 주소


## 나만의 오퍼레이터 만들기
오퍼레이터는 작업흐름의 논리상 단일 작업을 수행하는 원자단위이다.
오퍼레이터는 파이썬 클래스(BaseOperator의 하위 클래스) 로 작성되며 __init__함수 로 초기화 되고, execute 메서드를 통해 호출된다. 실패 시 AirflowSkipException을 발생시킨다.
또한 retries에서 설정된 횟수만큼 재시도하기때문에 execute 메서드는 멱등이어야 한다.

오퍼레이터는 다른 DAG에서 task로 재사용될 수 있으므로, 특별하게 취급된다.
airflow는 이러한 오퍼레이터들을 plugins 라는 디렉토리에서 디폴트로 취급하기때문에 airflow home에서 plugins라는 디렉터리를 생성하고 my_super_operators.py를 생성했다.

### 코드
```python
import logging

from airflow.models import BaseOperator
from airflow.plugins_manager import AirflowPlugin
from airflow.utils.decorators import apply_defaults

log = logging.getLogger(__name__)

class MySuperOperator(BaseOperator):

    @apply_defaults
    def __init__(self, operator_param, *args, **kwargs):
        self.operator_param = operator_param
        super(MySuperOperator, self).__init__(*args, **kwargs)

    def execute(self, context):
        log.info("im super operator!")
        log.info('operator_param: %s', self.operator_param)


class MyFirstPlugin(AirflowPlugin):
    name = "my_first_plugin"
    operators = [MySuperOperator]
```

MyFirstPlugin이라는 새로운 Airflow 플러그인을 정의하고 있다. 해당 plugin을 정해진 위치 (airflowhome/plugins)에 저장하면 dags하위 폴더에 밑에서 불러서 쓸 수 잇다.

새로 추가한 MyFirstPlugin 코드를 아까 작성한 hello_airflow.py 내 DAG에 추가해주고, 다시 scheduler와 web server 를 띄워주면 적용된 것을 확인할 수 있다.
![newTask](https://github.com/cacacoo/stack/blob/master/resource/images/airflow_1_task.png)

## 이제 Airflow에서 제공한 많은 예제들을 보며 하나하나 학습하자
[ ] task 결과를 어떻게 다음 task로 파람으로 넘겨주나?
[ ] task 를 병렬 실행하는 방법은?


