# Classese and Objects

##Classes
###class
자바 클래스랑 동일
```
class Invoice { … }
interface CommonCodeRepository : JpaRepository<CommonCode, Long>
// 내용이 없으면 {} 생략가능
```

###생성자
####기본 생성자와 하나 이상의 보조 생성자를 가질수 있다.
기본 생성자는 ( ) 안에 그냥 넣으면 된다. constructor 라는 키워드를 생략한다.
```
class Person constructor(firstName: String) { ... }
```

생성자 안에는 코드가 들어갈수 없으므로
클래스 바디안에 init 키워드를 사용해서 초기화를 시켜줄 수 있다.
```
class InitOrderDemo(name: String) {
    val firstProperty = "First property: $name".also(::println)

    init {
        println("First initializer block that prints ${name}")
    }
}
```

####보조 생성자는 constructor라는 키워드를 명시적으로 사용해서 선언한다.
클래스에 기본 생성자가 있으면 각 보조 생성자는 기본 보조 생성자에 직접 또는 보조 생성자를 통해 간접적으로 위임해야합니다. 동일한 클래스의 다른 생성자에 대한 위임은 this 키워드를 사용하여 수행됩니다.
```
class Person(val name: String) {
    constructor(name: String, parent: Person) : this(name) {
        parent.children.add(this)
    }
}
```

####init 구문이 보조 생성자의 실행에 앞선다
```
class Constructors {
    init {
        println("Init block")
    }

    constructor(i: Int) {
        println("Constructor")
    }
}
```

####클래스 인스턴스 생성
new 키워드가 없다.
```
val invoice = Invoice()
val customer = Customer("Joe Smith")
```

###클래스 맴버
-생성자 & init 구문
-함수들
-프로퍼티
-다중 & 내부 클래스
-객체 선언부


##상속
코틀린 모든 클레스가 Any 를 상속받는다. (자바의 Object)
Object랑 다른점이 있다면 Any는 아무것도 없다. equals(), hashCode() 이런거

###선언
상속은 명시적으로 클레스 헤더에 : 뒤에 붙여주면 된다
```
open class Base(p: Int)

class Derived(p: Int) : Base(p)
```

상속받는 클레스에 기본생성자가 없으면 super 키워드를 사용해준다
```
class MyView : View {
    constructor(ctx: Context) : super(ctx)

    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs)
}
```

###메소드 재정의 - overriding
어떤 메서드가 재정의가 가능한지를 open 키워드로 표시한다.
재정의 키워드는 override
더이상 재정의를 못하게 하고 싶을때는 final 키워드를 사용한다.
```
open class Base {
    open fun v() { ... }
    fun nv() { ... }
}
class Derived() : Base() {
    override fun v() { ... }
}

open class AnotherDerived() : Base() {
    final override fun v() { ... }
}
```

###상속 객체 생성 순서
객체 생성 요청 -> 부모 객체 init -> 부모 객체 정의 -> 상속 객체 init -> 상속 객체 정의
```
open class Base(val name: String) {

    init { println("Initializing Base") }

    open val size: Int =
        name.length.also { println("Initializing size in Base: $it") }
}

class Derived(
    name: String,
    val lastName: String
) : Base(name.capitalize().also { println("Argument for Base: $it") }) {

    init { println("Initializing Derived") }

    override val size: Int =
        (super.size + lastName.length).also { println("Initializing size in Derived: $it") }
}

fun main() {
    println("Constructing Derived(\"hello\", \"world\")")
    val d = Derived("hello", "world")
}

Constructing Derived("hello", "world")
Argument for Base: Hello
Initializing Base
Initializing size in Base: 5
Initializing Derived
Initializing size in Derived: 10
```

###Abstract Classes
open 이 명시된 함수는 override를 해줘야되는데, 나중으로 구현을 미루고 싶으면
```
open class Base {
    open fun f() {}
}

abstract class Derived : Base() {
    override abstract fun f()
}
```
이런식도 가능하다.

###Static 함수가 없다
그래서 어느곳에서건 인스턴스 생성없이 사용하고픈 메서드가 있으면
클래스 내 오브젝트 선언부에 맴버로 선언해서 사용하면 된다.
이때 companion object를 선언하는 것으로 가능해진다.
```
class MyClass {
    companion object Factory {
        fun create(): MyClass = MyClass()
    }
}

val instance = MyClass.creat
```

