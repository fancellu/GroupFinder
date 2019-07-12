package com.felstar.groupfinder

import org.scalatest.{Matchers, WordSpec}

class GroupFinderSpec extends WordSpec with Matchers {

  "remove1st" when {
    "handle empty list" should {
      "return empty list" in {
        GroupFinder.remove1st(List.empty[Int], 1) shouldBe empty
      }
    }

    "remove 1st element" should {
      "return correct list" in {
        GroupFinder.remove1st(List(1, 2, 3, 1), 1) shouldBe List(2, 3, 1)
      }
    }
    "remove only element" should {
      "return empty list" in {
        GroupFinder.remove1st(List(1), 1) shouldBe empty
      }
    }
  }

  "findGroups" when {
    import GroupFinder._
    "handle groups" should {
      "return empty everything" in {
        findGroups(List(), List()) shouldBe Groups(List(), List())
      }
      "return 1 pair" in {
        // looking for groups of 1+1
        findGroups(List(1, 2, 1, 2, 1), List(1, 1)) shouldBe Groups(List(List(1, 1)), List(2, 2, 1))
      }
      "return 2 groups" in {
        // looking for groups of 1+1+2
        findGroups(List(1, 2, 1, 2, 1, 1, 2), List(2, 1, 1)) shouldBe Groups(List(List(1, 2, 1), List(2, 1, 1)), List(2))
      }
      "return 0 matched" in {
        findGroups(List(1), List()) shouldBe Groups(List(), List(1))
      }
      "return correctly even with strings" in {
        findGroups(List("one", "two", "two", "one", "three"), List("one", "two")) shouldBe
          Groups(List(List("one", "two"), List("two", "one")), List("three"))
      }
      "handle more complex types" in {
        case class Product(name: String, price: Double)
        findGroups(List(Product("banana", 10), Product("banana", 10), Product("apple", 5)), List.fill(2)(Product("banana", 10))) shouldBe
          Groups(List(List(Product("banana", 10), Product("banana", 10))), List(Product("apple", 5)))
      }
    }
  }
}
