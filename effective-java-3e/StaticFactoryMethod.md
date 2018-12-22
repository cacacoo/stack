# 정적 팩토리 메서드
클래스의 인스턴스를 얻는 전통적인 수단인 Public 생성자 대신 정적 팩토리 메서드를 제공하는 것도 고려해보자.

```java
public static boolean valueOf(boolean a) {
  return a? Boolean.TRUE : Boolean.FALSE;
}
```

## 정적 팩토리 메서드가 어떤 점에서 public 생성자 보다 좋을까?
- 이름을 가질 수 있다. 이름을 명시적으로 표현해서 알아보기가 더 쉽다.
- 호출될 떄마다 인스턴스를 새로 생성하지 않아도 된다. 불변 클래스를 통한 인스턴스를 미리 생성해두고 불필요한 객체 생성을 피할 수 있다.
- 반환 타입의 하위 타입 객체를 반환할 수 있다. 
- 입력 매개변수에 따라 매번 다른 클래스의 객체를 반환할 수 있다. (ex. java.util.EnumSet)
- 작성 시점에는 반환할 객체의 클래스가 존재하지 않아도 된다. 서비스 제공자 프레임워크를 만드는 근간이 된다.

> 서비스 제공자 프레임워크(service provider framwork)? 
> 대표적으로 JDBC 가 있다. 서비스의 구현체인 provider와 이 구현체들을 클라이언트에 제공하는 역할을 프레임워크가 통제하여 클라이언트를 구현체로부터 분리한다.
> service interface : 구현체의 동작을 정의 (JDBC 에서 Connection)
> provider registration API : 제공자가 구현체를 등록할 때 사용하는 제공자 등록 API (JDBC에서 DriverManager.registerDriver)
> service access API : 클라이언트가 서비스 인스턴스를 얻을 때 사용하는 API (JDBC에서 DriverManager.getConnection)

## 어떤 단점이 있을까?
- 상속을 할때 public, protected 생성자가 필요해서, 정적 팩토리 메서드만 제공하면 하위 클래스를 제공할 수 없다
- 생성자처럼 API 설명에 드러나지 않아 프로그래머가 찾기 어려울 수 있다.






  
