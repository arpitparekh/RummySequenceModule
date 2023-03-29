package com.example.rummysequencemodule

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rummysequencemodule.databinding.ActivityMainBinding
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    var cardList  = ArrayList<AssignedCards>()
    lateinit var  cardAdapter : CardAdapter
    lateinit var binding : ActivityMainBinding
    var impureSequenceCount = 0
    var pureSequenceCount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        // theme
        // thacall thein

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        cardList.add(AssignedCards("1","Ace","B","",R.drawable.one,"1"))
        cardList.add(AssignedCards("2","Two","B","",R.drawable.two,"2"))
        cardList.add(AssignedCards("3","Three","B","",R.drawable.three,"3"))
        cardList.add(AssignedCards("4","Four","B","",R.drawable.four,"4"))
        cardList.add(AssignedCards("5","Five","B","",R.drawable.five,"5"))
        cardList.add(AssignedCards("1","Ace","B","",R.drawable.six,"1"))
        cardList.add(AssignedCards("1","Ace","B","",R.drawable.seven,"1"))

        cardList.add(AssignedCards("53","Black Joker","B","",R.drawable.fifty_three,"14"))

        cardAdapter = CardAdapter(cardList)
        binding.rvCards.layoutManager = GridLayoutManager(this,10)
        binding.rvCards.adapter = cardAdapter

        binding.tvStatus.text = getGroupStatus(cardList,"Black ")

    }

    fun String.equalsIgnoreCase(other: String?): Boolean {
        if (other == null) {
            return false
        }
        return this.equals(other, true)
    }

    fun getGroupStatus(cardsSuitCList: List<AssignedCards>, jokerName: String?): String {
        var firstGroupIs = ""
        val oneList: MutableList<AssignedCards> = ArrayList()
        oneList.addAll(cardsSuitCList)

        oneList.sortBy { it.suit_id }

        if (oneList.size >= 3) {
            if (isSameSuit(oneList)) {
                if (isConsecutive(oneList)) {
                    firstGroupIs = "PURE"
                    pureSequenceCount = pureSequenceCount + 1
                } else {
                    var jokerPos = -1
                    var jokerCount = 0
                    val pureJokerCount = 0
                    val pureJokerPos = 1
                    for (i in oneList.indices) {
                        if (oneList[i].name.equalsIgnoreCase(jokerName)) {
                            jokerPos = i
                            jokerCount = jokerCount + 1
                        }
                    }
                    if (jokerCount == 1) {
                        oneList.removeAt(jokerPos)
                        if (isSameSuit(oneList)) {
                            if (oneList.size >= 2) {
                                if (oneList.size == 2) {
                                    val firstPoint: Int = oneList[0].suit_id.toInt()
                                    val lastPoint: Int = oneList[1].suit_id.toInt()
                                    val resultNumber = lastPoint - firstPoint
                                    if (firstPoint == 1 && lastPoint == 13) {
                                        firstGroupIs = "IMPURE"
                                        impureSequenceCount = impureSequenceCount + 1
                                    } else if (resultNumber == 2 || resultNumber == 1) {
                                        firstGroupIs = "IMPURE"
                                        impureSequenceCount = impureSequenceCount + 1
                                    }
                                } else {
                                    if (jokerPos == 0 || jokerPos == oneList.size - 1) {
                                        if (isConsecutive(oneList)) {
                                            firstGroupIs = "IMPURE"
                                            impureSequenceCount = impureSequenceCount + 1
                                        } else {
                                            val currentValue = 1
                                            var missingCount = 0
                                            var index = 0
                                            for (i in oneList[0].suit_id.toInt() until oneList[oneList.size - 1].suit_id.toInt()) {
                                                if (i == oneList[index].suit_id.toInt()) {
                                                    index++
                                                } else {
                                                    missingCount = missingCount + 1
                                                }
                                            }
                                            if (missingCount == 1) {
                                                firstGroupIs = "IMPURE"
                                                impureSequenceCount = impureSequenceCount + 1
                                            }
                                        }
                                    } else {
                                        val currentValue = 1
                                        var missingCount = 0
                                        for (i in oneList[0].suit_id.toInt() until oneList[oneList.size - 1].suit_id.toInt()) {
                                            if (i != oneList[i].suit_id.toInt()) {
                                                missingCount = missingCount + 1
                                            }
                                        }
                                        if (missingCount == 1) {
                                            firstGroupIs = "IMPURE"
                                            impureSequenceCount = impureSequenceCount + 1
                                        } else {
                                            val firstPoint: Int = oneList[0].suit_id.toInt()
                                            val listOne: MutableList<AssignedCards> = ArrayList()
                                            val listTwo: MutableList<AssignedCards> = ArrayList()
                                            for (i in 0 until jokerPos) {
                                                val selectedCard: AssignedCards = oneList[i]
                                                listOne.add(selectedCard)
                                            }
                                            for (i in jokerPos until listOne.size) {
                                                val selectedCard: AssignedCards = oneList[i]
                                                listTwo.add(selectedCard)
                                            }
                                            val lastPoint: Int =
                                                oneList[oneList.size - 1].suit_id.toInt()
                                            if (lastPoint == 13 && listOne.size == 1 && isContainAce(
                                                    listOne
                                                ) && isConsecutive(listTwo)
                                            ) {
                                                firstGroupIs = "IMPURE"
                                                impureSequenceCount = impureSequenceCount + 1
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        val lastSuitId: String = oneList[oneList.size - 1].suit_id
                        var count = 0
                        var pos = -1
                        for (i in oneList.indices) {
                            if (oneList[i].name.equalsIgnoreCase("Ace")) {
                                count = count + 1
                                pos = i
                            }
                        }
                        if (lastSuitId.equals("13", ignoreCase = true) && count == 1) {
                            oneList.removeAt(pos)
                            if (oneList.size >= 2) {
                                if (isConsecutive(oneList)) {
                                    firstGroupIs = "PURE"
                                    pureSequenceCount = pureSequenceCount + 1
                                }
                            }
                        }
                    }
                }
            } else {
                var jokerPos = -1
                var jokerCount = 0
                var pureJokerCount = 0
                var pureJokerPos = 1
                for (i in oneList.indices) {
                    if (oneList[i].name.equalsIgnoreCase(jokerName)) {
                        jokerPos = i
                        jokerCount = jokerCount + 1
                    }
                    if (oneList[i].name.equalsIgnoreCase("Black Joker") || oneList[i].name.equalsIgnoreCase(
                            "Red Joker"
                        )
                    ) {
                        pureJokerCount = pureJokerCount + 1
                        pureJokerPos = i
                    }
                }
                if (jokerCount == 1) {
                    oneList.removeAt(jokerPos)
                    if (isSameSuit(oneList)) {
                        if (oneList.size >= 2) {
                            if (oneList.size == 2) {
                                val firstPoint: Int = oneList[0].suit_id.toInt()
                                val lastPoint: Int = oneList[1].suit_id.toInt()
                                val resultNumber = lastPoint - firstPoint
                                if (firstPoint == 1 && lastPoint == 13) {
                                    firstGroupIs = "IMPURE"
                                    impureSequenceCount = impureSequenceCount + 1
                                } else if (resultNumber == 2 || resultNumber == 1) {
                                    firstGroupIs = "IMPURE"
                                    impureSequenceCount = impureSequenceCount + 1
                                }
                            } else {
                                if (jokerPos == 0 || jokerPos == oneList.size - 1) {
                                    if (isConsecutive(oneList)) {
                                        firstGroupIs = "IMPURE"
                                        impureSequenceCount = impureSequenceCount + 1
                                    } else {
                                        var currentValue = 1
                                        var missingCount = 0
                                        for (i in oneList.indices) {
                                            if (oneList[i].suit_id.toInt() != currentValue) {
                                                for (j in currentValue until oneList[i].suit_id.toInt()) {
                                                    missingCount = missingCount + 1
                                                }
                                            }
                                            currentValue = oneList[i].suit_id.toInt() + 1
                                        }
                                        if (missingCount == 1) {
                                            firstGroupIs = "IMPURE"
                                            impureSequenceCount = impureSequenceCount + 1
                                        }
                                    }
                                } else {
                                    var currentValue = 1
                                    var missingCount = 0
                                    for (i in oneList.indices) {
                                        if (oneList[i].suit_id.toInt() != currentValue) {
                                            for (j in currentValue until oneList[i].suit_id.toInt()) {
                                                missingCount = missingCount + 1
                                            }
                                        }
                                        currentValue = oneList[i].suit_id.toInt() + 1
                                    }
                                    if (missingCount == 1) {
                                        firstGroupIs = "IMPURE"
                                        impureSequenceCount = impureSequenceCount + 1
                                    } else {
                                        val listOne: MutableList<AssignedCards> = ArrayList()
                                        val listTwo: MutableList<AssignedCards> = ArrayList()
                                        for (i in 0 until jokerPos) {
                                            val selectedCard: AssignedCards = oneList[i]
                                            listOne.add(selectedCard)
                                        }
                                        for (i in jokerPos until listOne.size) {
                                            val selectedCard: AssignedCards = oneList[i]
                                            listTwo.add(selectedCard)
                                        }
                                        val lastPoint: Int =
                                            oneList[oneList.size - 1].suit_id.toInt()
                                        if (lastPoint == 13 && listOne.size == 1 && isContainAce(
                                                listOne
                                            ) && isConsecutive(listTwo)
                                        ) {
                                            firstGroupIs = "IMPURE"
                                            impureSequenceCount = impureSequenceCount + 1
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else if (pureJokerCount == 1) {
                    oneList.removeAt(pureJokerPos)
                    if (isSameSuit(oneList)) {
                        if (oneList.size >= 2) {
                            if (isConsecutive(oneList)) {
                                firstGroupIs = "IMPURE"
                                impureSequenceCount = impureSequenceCount + 1
                            } else {
                                if (oneList.size == 2) {
                                    val firstPoint: Int = oneList[0].suit_id.toInt()
                                    val lastPoint: Int = oneList[1].suit_id.toInt()
                                    val resultNumber = lastPoint - firstPoint
                                    if (firstPoint == 1 && lastPoint == 13) {
                                        firstGroupIs = "IMPURE"
                                        impureSequenceCount = impureSequenceCount + 1
                                    } else if (resultNumber == 2 || resultNumber == 1) {
                                        firstGroupIs = "IMPURE"
                                        impureSequenceCount = impureSequenceCount + 1
                                    }
                                } else {
                                    var currentValue = 1
                                    var missingCount = 0
                                    for (i in oneList.indices) {
                                        if (oneList[i].suit_id.toInt() != currentValue) {
                                            for (j in currentValue until oneList[i].suit_id.toInt()) {
                                                missingCount = missingCount + 1
                                            }
                                        }
                                        currentValue = oneList[i].suit_id.toInt() + 1
                                    }
                                    if (missingCount == 1) {
                                        firstGroupIs = "IMPURE"
                                        impureSequenceCount = impureSequenceCount + 1
                                    } else {
                                        val lastSuitId: String = oneList[oneList.size - 1].suit_id
                                        var count = 0
                                        var pos = -1
                                        for (i in oneList.indices) {
                                            if (oneList[i].name.equalsIgnoreCase("Ace")) {
                                                count = count + 1
                                                pos = i
                                            }
                                        }
                                        if (lastSuitId.equals(
                                                "13",
                                                ignoreCase = true
                                            ) && count == 1
                                        ) {
                                            oneList.removeAt(pos)
                                            if (oneList.size >= 2) {
                                                if (isConsecutive(oneList)) {
                                                    firstGroupIs = "IMPURE"
                                                    impureSequenceCount = impureSequenceCount + 1
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (firstGroupIs.equals("", ignoreCase = true)) {
                if (isSameName(oneList)) {
                    if (isDifferentSuit(oneList)) {
                        firstGroupIs = "SET"
                    }
                } else {
                    var jokerPos = -1
                    var jokerCount = 0
                    var pureJokerCount = 0
                    var pureJokerPos = 1
                    for (i in oneList.indices) {
                        if (oneList[i].name.equalsIgnoreCase(jokerName)) {
                            jokerPos = i
                            jokerCount = jokerCount + 1
                        }
                        if (oneList[i].name.equalsIgnoreCase("Black Joker") || oneList[i].name.equalsIgnoreCase(
                                "Red Joker"
                            )
                        ) {
                            pureJokerCount = pureJokerCount + 1
                            pureJokerPos = i
                        }
                    }
                    if (jokerCount == 1) {
                        oneList.removeAt(jokerPos)
                        if (isSameName(oneList)) {
                            if (isDifferentSuit(oneList)) {
                                firstGroupIs = "SET"
                            }
                        }
                    } else if (pureJokerCount == 1) {
                        oneList.removeAt(pureJokerPos)
                        if (isSameName(oneList)) {
                            if (isDifferentSuit(oneList)) {
                                firstGroupIs = "SET"
                            }
                        }
                    }
                }
            }
        }
        return firstGroupIs
    }

    fun isSameSuit(list: List<AssignedCards>): Boolean {
        val firstGroupCard: String = list[0].suit
        for (i in 1 until list.size) {
            if (!firstGroupCard.equals(list[i].suit, ignoreCase = true)) {
                return false
            }
        }
        return true
    }

    fun isConsecutive(list: List<AssignedCards>): Boolean {
        for (i in 0 until list.size - 1) {
            if (list[i].suit_id.toInt() != list[i + 1].suit_id.toInt() - 1) {
                return false
            }
        }
        return true
    }

    fun isSameName(list: List<AssignedCards>): Boolean {
        val firstGroupCard: String = list[0].name
        for (i in 1 until list.size) {
            if (!firstGroupCard.equals(list[i].name, ignoreCase = true)) {
                return false
            }
        }
        return true
    }

    fun isDifferentSuit(list: List<AssignedCards>): Boolean {
        val firstGroupCard: String = list[0].suit
        for (i in 1 until list.size) {
            if (firstGroupCard.equals(list[i].suit, ignoreCase = true)) {
                return false
            }
        }
        return true
    }

    fun isContainAce(list: List<AssignedCards>): Boolean {
        var isAceAvailable = false
        for (i in list.indices) {
            if (list[i].name.equalsIgnoreCase("Ace")) {
                isAceAvailable = true
            }
        }
        return isAceAvailable
    }

    fun aceCount(list: List<AssignedCards>): Int {
        var count = 0
        for (i in list.indices) {
            if (list[i].name.equalsIgnoreCase("Ace")) {
                count = count + 1
            }
        }
        return count
    }

    fun isContainJoker(list: List<AssignedCards>, jokerName: String?): Boolean {
        var isAceAvailable = false
        for (i in list.indices) {
            if (list[i].name.equalsIgnoreCase(jokerName)) {
                isAceAvailable = true
            }
        }
        return isAceAvailable
    }

}