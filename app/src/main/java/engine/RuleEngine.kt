package engine
import kotlinx.coroutines.delay

class RuleEngine {
    var currentState: String = "INIT"
        private set

    data class Rule(
        val keyword: String,
        val requiredState: String,
        val response: String,
        val nextState: String,
        val typingDelayMs: Long = 400
    )

    private val rules = listOf(
        Rule("pass0", "INIT",  "初期突破",  "STATE1"),
        Rule("pass1", "STATE1","1つ目突破","STATE2"),
        Rule("pass2", "STATE2","2つ目突破","STATE3"),
        Rule("pass3", "STATE3","3つ目突破","STATE4"),
        Rule("pass4", "STATE4","4つ目突破","STATE5"),
    )

    fun match(input: String): Matched {
        if (currentState == "STATE5") return Matched.Ignore
        val rule = rules.firstOrNull { it.requiredState == currentState && input.trim() == it.keyword }
        return if (rule != null) Matched.Hit(rule) else Matched.Fallback
    }

    sealed class Matched {
        data class Hit(val rule: Rule): Matched()
        object Fallback: Matched()
        object Ignore: Matched()
    }

    suspend fun advance(hit: Hit, onEmit: suspend (String) -> Unit) {
        delay(hit.rule.typingDelayMs)
        onEmit(hit.rule.response)
        currentState = hit.rule.nextState
    }
}
