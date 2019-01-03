# Kotlin Basic
Link: [kotlin 공식문서](https://kotlinlang.org/docs/reference/basic-types.html, "kotlin doc")
안드로이드 공식 언어, 인텔리J에서 적극 지원 중
간결함(자바보다), 함수형

###변수 선언 방식
val : read-only
var : mutable
선언 형태 - [val or var] [변수명] : [데이터형][? means nullable] = [값]

###동등 비교
```
val a: Int = 10000
println(a === a) // Prints 'true'
val boxedA: Int? = a
val anotherBoxedA: Int? = a
println(boxedA === anotherBoxedA) // !!!Prints 'false'!!!
println(boxedA == anotherBoxedA) // !!!Prints ’true'!!!
```
자바처럼 프리미티브 할당이 아니라 객체로 전달된다.
레퍼런스가 할당되서 동일성 비교는 false, 동등성 비교는 true가 된다

###명시적 변환
```
val i = 1L + 3 // Long + Int => Long
```

###문자열
Char 타입은 숫자로 취급가능
```
fun check(c: char) {
    for(c == 1) {
       ...
    }
}
```

###Arrays
Array 클레스에 get,set,size 가 구현되어 있다
arrayOf(1,2,3) => [1,2,3] 만들어짐 혹은
```
// Creates an Array<String> with values ["0", "1", "4", "9", "16"]
val asc = Array(5, { i -> (i * i).toString() })
asc.forEach { println(it) }
```
size는 따로 선언할 필요가 없다

###Strings
for(c in str) 가능
여러줄을 표기할때 파이썬처럼 """ 을 해도되고, 자바처럼  \n 을 써도 된다
자바스크립트처럼 템플릿 기능이 있다.
```
val s = "abc"
println("$s.length is ${s.length}") // prints "abc.length is 3"
```

###Packages
코틀린 주요 패키지가 이미 디폴트로 임포트 되어있다. Java.Collection 이런거 임포트 안해도 된다.

###제어문
####if
리턴을 표기안하고 블록 마지막 줄 값이 리턴된다
```
val max = if (a > b) {
    print("Choose a")
    a
} else {
    print("Choose b")
    b
}
```

####when
switch 연산을 대체한다.
조건절에 값대신 함수를 넣는것도 가능하다!
```
when (x) {
    1 -> print("x == 1")
    2 -> print("x == 2")
    else -> { // Note the block
        print("x is neither 1 nor 2")
    }
}

when (x) {
    parseInt(s) -> print("s encodes x")
    else -> print("s does not encode x")
}
```

####For
인덱스때문에 for를 쓰는 경우가 많았는데 문법으로 지원해준다
```
for ((index, value) in array.withIndex()) {
    println("the element at $index is $value")
}
```

###Returns and Jumps
라벨(@) 을 두면 그쪽으로 jump 가능
```
loop@ for (i in 1..100) {
    for (j in 1..100) {
        if (...) break@loop
    }
}
```

응용해서
```
fun foo() {
    listOf(1, 2, 3, 4, 5).forEach lit@{
        if (it == 3) return@lit // local return to the caller of the lambda, i.e. the forEach loop
        print(it)
    }
    print(" done with explicit label")
}

// 1245 done with explicit label
```


