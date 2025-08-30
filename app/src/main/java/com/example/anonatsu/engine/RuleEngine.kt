package com.example.anonatsu.engine

import kotlinx.coroutines.delay // ← これが無いと unresolved になります

class RuleEngine(
    // テスト時に遅延を0に差し替えられるように
    private val delayMsProvider: (Rule) -> Long = { it.typingDelayMs }
) {
    var currentState: String = "INIT"
        private set

    data class Rule(
        val keyword: String,        // 期待する入力（すべて lowercase で持つ）
        val requiredState: String,  // この状態のときのみ有効
        val response: String,       // BOTの返答
        val nextState: String,      // 遷移先
        val typingDelayMs: Long = 400
    )

    private val rules = listOf(
        Rule("pass0", "INIT",  "初期突破",  "STATE1"),
        Rule("pass1", "STATE1","1つ目突破","STATE2"),
        Rule("pass2", "STATE2","2つ目突破","STATE3"),
        Rule("pass3", "STATE3","3つ目突破","STATE4"),
        Rule("pass4", "STATE4","4つ目突破","STATE5"),
    )

    private fun keyOf(input: String) = input.trim().lowercase()

    sealed class Matched {
        data class Hit(val rule: Rule): Matched()
        object Fallback: Matched() // 「失敗しました」
        object Ignore: Matched()   // STATE5 での完全無反応
    }

    fun match(input: String): Matched {
        if (currentState == "STATE5") return Matched.Ignore
        val key = keyOf(input)
        val rule = rules.firstOrNull { it.requiredState == currentState && it.keyword == key }
        return if (rule != null) Matched.Hit(rule) else Matched.Fallback
    }

    // 既存の RuleEngine クラスの中
    fun reset() {
        currentState = "INIT"
    }


    suspend fun advance(hit: Matched.Hit, onEmit: suspend (String) -> Unit) {
        delay(delayMsProvider(hit.rule))
        onEmit(hit.rule.response)
        currentState = hit.rule.nextState
    }
}
