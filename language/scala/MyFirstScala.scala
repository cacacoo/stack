object MyFirstScala {
  def abs(n: Int): Int =
    if(n < 0) -n
    else n

  private def formatAbs(x: Int) = {
    val msg = "The absolute value of %d is %d"
    msg.format(x, abs(x))
  }

  def main(args: Array[String]): Unit = {
    println(formatAbs(-42))
    println(formatFactorial(6))
    println(fib(3))
  }

  def factorial(n: Int) :Int = {
    def go(n: Int, acc: Int): Int =
      if(n <= 0) acc
      else go(n-1, n*acc)
    go(n, 1)
  }

  private def formatFactorial(x : Int) = {
    val msg = "The factirial value of %d is %d"
    msg.format(x, factorial(x))
  }

  def fib(n: Int) :Int = {
    if(n == 0) 0
    else if(n == 1) 1
    else fib(n-1) + fib(n-2)
  }

  // higher order function
  // f는 단형적 함수(monomorphic function), 하나의 인,아웃만 정의되어있다
  def formatResult(name: String, x: Int, f: Int => Int) = {
    val msg = "The %s value of %d is %d"
    msg.format(name, x, f(x))
  }

  def checkEqual[A](a: A, keyword: A): Boolean =
    a == keyword
  // polymorphic function
  def findFirst[A](as: Array[A], P: (A, A) => Boolean, keyword: A): Int = {
    @annotation.tailrec
    def loop(n : Int): Int =
      if(n >= as.length) -1
      else if (P(as(n), keyword)) n
      else loop(n + 1)
      
    loop(0)
  }

  // curring : 여러개의 인자를 가진 함수를 호출 할 경우,
  // 파라미터의 수보다 적은 수의 파라미터를 인자로 받으면 누락된 파라미터를 인자로 받는 기법
  // 실행구문 : curry((a: Int, b: Int) => a+b)(2)(3)  //result = 5
  def curry[A,B,C](f: (A, B) => C): A => (B => C) =
    (a: A) => (b: B) => f(a, b)

  // curry 변환을 역으로 수행하는 고차 함수 uncurry 작성
  def uncurry[A,B,C](f: A => B => C): (A, B) => C =
    (a: A, b: B) => f(a)(b)

  // 함수 합성
  def compose[A,B,C](f:B => C, g:A => B): A => C =
    (a: A) => f(g(a))

}

