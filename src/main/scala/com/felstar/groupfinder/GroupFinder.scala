package com.felstar.groupfinder

import scala.annotation.tailrec

object GroupFinder {

  case class Groups[A](matched: List[List[A]], unmatched: List[A])

  def remove1st[A](list: List[A], a: A): List[A] = {
    val (part1, part2) = list span (_ != a)
    part1 ::: part2.drop(1)
  }

  // findGroup returns a tuple of (found, unmatched) lists
  def findGroup[A](originalList: List[A], originalCandidates: List[A]): (List[A], List[A]) = {

    @tailrec
    def findGroupRec(
        list: List[A],
        candidates: List[A],
        found: List[A] = Nil,
        unmatched: List[A] = Nil
    ): (List[A], List[A]) =
      list match {
        // if h is in candidates, add h to found, remove1st h from candidates
        case h :: tail if candidates.contains(h) => findGroupRec(tail, remove1st(candidates, h), h :: found, unmatched)
        // if h is not in candidates, add h to unmatched
        case h :: tail => findGroupRec(tail, candidates, found, h :: unmatched)
        // no more to process, if complete group not found then leave untouched else return found and unmatched
        case _ =>
          if (found.size != originalCandidates.size) (List.empty[A], originalList)
          else (found.reverse, unmatched.reverse)
      }

    findGroupRec(originalList, originalCandidates)
  }

  @tailrec
  def findGroups[A](list: List[A], candidates: List[A], matched: List[List[A]] = Nil): Groups[A] =
    findGroup(list, candidates) match {
      // if no more found, return matched and unmatched
      case (Nil, unmatched) => Groups(matched.reverse, unmatched)
      // otherwise, add found to foundList and recurse
      case (found, unmatched) => findGroups(unmatched, candidates, found :: matched)
    }

}
