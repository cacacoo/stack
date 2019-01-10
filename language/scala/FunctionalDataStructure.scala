object FunctionalDataStructure {

  def listToString(list: List[String]): String = list match {
      case s :: rest => s + " " + listToString(rest)
      case Nil => ""
  }

  def removeFirst[A](list: List[A]): List[A] = list match {
    case Nil => Nil
    case first :: rest => rest
  }

  def drop[A](list: List[A], n: Int): List[A] = list match {
    case Nil => Nil
    case head :: rest => {
      if(n == 1) rest
      else drop(rest, n-1)
    }
  }

  def dropWhile[A](list: List[A])(f: A => Boolean): List[A] = list match {
    case head :: rest if f(head) => dropWhile(rest)(f)
    case _ => list
  }

}

