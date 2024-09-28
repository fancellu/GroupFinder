package com.felstar.groupfinder

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class GroupFinderSpec extends AnyWordSpec with Matchers {

  "remove1st" when {
    import GroupFinder._
    "handle empty list" should {
      "return empty list" in {
        remove1st(List.empty[Int], 1) shouldBe empty
      }
    }

    "remove 1st element" should {
      "return correct list" in {
        remove1st(List(1, 2, 3, 1), 1) shouldBe List(2, 3, 1)
      }
    }
    "remove only element" should {
      "return empty list" in {
        remove1st(List(1), 1) shouldBe empty
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
        findGroup(List(1, 2, 1, 2, 1), List(1, 1)) shouldBe (List(1, 1),List(2, 2, 1))
        findGroups(List(1, 2, 1, 2, 1), List(1, 1)) shouldBe Groups(List(List(1, 1)), List(2, 2, 1))
      }
      "return 2 groups" in {
        // looking for groups of 1+1+2
        findGroup(List(1, 2, 1, 2, 1, 1, 2), List(2, 1, 1)) shouldBe (List(1, 2, 1),List(2, 1, 1, 2))
        findGroup(List(2, 1, 1, 2), List(2, 1, 1)) shouldBe (List(2, 1, 1),List(2))
        findGroups(List(1, 2, 1, 2, 1, 1, 2), List(2, 1, 1)) shouldBe Groups(List(List(1, 2, 1), List(2, 1, 1)), List(2))
      }
      "return 0 matched" in {
        findGroups(List(1), List()) shouldBe Groups(List(), List(1))
        findGroups(List(1), List(2)) shouldBe Groups(List(), List(1))
        findGroup(List(2, 2, 1), List(1, 1)) shouldBe (List(),List(2, 2, 1))
      }
      "return correctly even with strings" in {
        findGroups(List("one", "two", "two", "one", "three"), List("one", "two")) shouldBe
          Groups(List(List("one", "two"), List("two", "one")), List("three"))
      }
      "handle more complex types" in {
        // partial equality, case classes only check equality for 1st parameter list
        // https://gist.github.com/fancellu/ec184ab05c41c76e7d614310af508fe1
        case class Product(name: String)(val price: Double)
        // we can choose to only supply the name, i.e. partially applied
        val banana=Product("banana") _

        // note, price is not used for equality, but is still very much present
        val groups=findGroups(List(banana(10), banana(8), Product("apple")(5)), List.fill(2)(banana(0)))
        groups.matched shouldBe List(List(banana(0), banana(0)))
        groups.unmatched shouldBe List(Product("apple")(0))
        groups.matched.flatMap(_.map(_.price)).sum shouldBe 18
      }
    }
  }
}
