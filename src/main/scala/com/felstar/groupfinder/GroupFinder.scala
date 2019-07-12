package com.felstar.groupfinder

import scala.annotation.tailrec

object GroupFinder {

  case class Groups[A](matched: List[List[A]], unmatched: List[A])

   def remove1st[A](list: List[A], a: A): List[A] = {
      val (part1, part2) = list span (_ != a)
      part1 ::: part2.drop(1)
    }

    def findGroup[A](originalList: List[A], originalCandidates: List[A]): (List[A], List[A]) = {

      @tailrec
      def findGroupRec(list: List[A], candidates: List[A], found: List[A] = Nil, unchanged: List[A] = Nil): (List[A], List[A]) =
        list match {
          case h :: tail if candidates.contains(h) => findGroupRec(tail, remove1st(candidates, h), h :: found, unchanged)
          case h :: tail => findGroupRec(tail, candidates, found, h :: unchanged)
          case _ => if (found.size != originalCandidates.size) (List.empty[A], originalList) else (found.reverse, unchanged.reverse)
        }

      findGroupRec(originalList, originalCandidates)
    }

    @tailrec
    def findGroups[A](list: List[A], candidates: List[A], foundlist: List[List[A]] = Nil): Groups[A] =
      findGroup(list, candidates) match {
        case (Nil, unchanged) => Groups(foundlist.reverse, unchanged)
        case (found, unchanged) => findGroups(unchanged, candidates, found :: foundlist)
      }

}
