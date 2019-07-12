package com.felstar.groupfinder

import com.felstar.groupfinder.GroupFinder.Groups
import org.scalatest.{Matchers, WordSpec}

class GroupFinderSpec extends WordSpec with Matchers {

  "ByList.remove1st" when {
    "handle empty list" should {
      "return empty list" in {
        GroupFinder.ByList.remove1st(List.empty[Int], 1) shouldBe empty
      }
    }

    "remove 1st element" should {
      "return correct list" in {
        GroupFinder.ByList.remove1st(List(1, 2, 3, 1), 1) shouldBe List(2, 3, 1)
      }
    }
    "remove only element" should {
      "return empty list" in {
        GroupFinder.ByList.remove1st(List(1), 1) shouldBe empty
      }
    }
  }

  "BySet.findGroups" when {
    // we look for groups of 1+2+3
    val set123 = Set(1, 2, 3)
    import GroupFinder.BySet._
    "handle groups" should {
      "return empty everything" in {
        findGroups(List(), set123) shouldBe Groups(List(), List())
      }
      "return unmatched with unchanged input" in {
        findGroups(List(1, 4), set123) shouldBe Groups(List(), List(1, 4))
      }
      "return 2 groups with some unmatched" in {
        findGroups(List(2, 1, 3, 1, 4, 2, 1, 2, 3), set123) shouldBe
          Groups(List(List(2, 1, 3), List(1, 2, 3)), List(4, 1, 2))
      }
      "return correctly even with strings" in {
        findGroups(List("one", "two", "two", "one", "three"), Set("one", "two")) shouldBe
          Groups(List(List("one", "two"), List("two", "one")), List("three"))
      }
    }
  }

  "ByList.findGroups" when {
    import GroupFinder.ByList._
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
      "handle more complex types" in{
        case class Product(name: String, price: Double)
        findGroups(List(Product("banana",10),Product("banana",10),Product("apple",5)), List.fill(2)(Product("banana",10))) shouldBe
          Groups(List(List(Product("banana",10),Product("banana",10))), List(Product("apple",5)))
      }
    }
  }


}
