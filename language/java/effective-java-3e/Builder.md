# 생성자에 매개변수가 많은 경우에 빌더를 고려하자
정적 패터리와 생성자에는 매개변수가 많은 경우 적절히 대응하기 어렵다는 단점이 있다.

## 매개변수가 많은 경우 생성 방식
- 점층적 생성자 : 5개라면 1~5개까지 모두 생성자를 만든다. 인자의 타입이 비슷하면 순서를 헷갈릴 수 있고 보기에도 좋지 않다.
- 자바빈즈 : get,set으로 객체 생성 이후 파라미터를 셋팅하는 방식으로, 가변적이라는 점이 단점이다. 어디에서 어떻게 고쳐졌는지는 코드를 따라가봐야 알수있게된다.
- 빌더 : 객체 내부에 빌더를 두어 빌더에 생성을 위임한다. 객체 자체를 바로 생성하지 못하게 막는다.

## 빌더를 썼을 때의 장점은?
- 불변성. 만들어진 이후에는 수정 불가로 두어 개발자의 마음을 안정시킬 수 있다.
- 보기에 좋다. 

## 의문점
- 빌더를 사용해서 만든 객체를 사용하다가, 서비스 로직에서 추가 프로퍼티를 저장할 필요가 있는 경우에 어떻게 하면 좋을까? 
새로운 객체를 생성한다 or 가변프로퍼티를 추가한다(setter를 두어)
