package com.yangian.numsum.feature.calculator

import androidx.lifecycle.ViewModel
import com.yangian.numsum.feature.calculator.exprk.Expressions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CalculatorViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    fun clearCalculator() {
        _uiState.value = CalculatorUiState()
    }

    fun deleteLastCharacter() {
        val expression = _uiState.value.expression
        if (expression.isNotEmpty())
            _uiState.update { currentState ->
                currentState.copy(
                    expression = currentState.expression.dropLast(1),
                    length = currentState.length - 1
                )
            }
    }

    fun appendNumber(number: Char) {
        val expression = _uiState.value.expression

        if (expression.isEmpty() ||
            expression.last() != ')'
        ) {
            _uiState.update { currentState ->
                currentState.copy(
                    expression = currentState.expression + number,
                    length = currentState.length + 1
                )
            }
        }
    }

    fun appendOperator(operator: Char) {

        val expression = _uiState.value.expression

        if (expression.isEmpty()) {
            // For empty expression only + and - are allowed
            if (operator in listOf('+', '-')) {
                _uiState.update { currentState ->
                    currentState.copy(
                        expression = operator.toString(),
                        length = 1
                    )
                }
            }
        } else {
            // If the expression is not empty then check for the last character.
            val lastChar = expression.last()

            if (lastChar in listOf('×', '÷', '(') &&
                operator in listOf('+', '-')
            ) {
                // If the last char is '×' or '÷' or '(' then '+' or '-'can be appended
                _uiState.update { currentState ->
                    currentState.copy(
                        expression = currentState.expression + operator,
                        length = currentState.length + 1
                    )
                }
            } else if (lastChar in listOf('+', '-')) {
                if (operator in listOf('+', '-')) {
                    // If the last char is '+' or '-' then replace the last char with operator if the operator is '+' or '-'
                    _uiState.update { currentState ->
                        currentState.copy(
                            expression = expression.dropLast(1) + operator,
                            length = currentState.length + 1
                        )
                    }
                }
            } else if (lastChar != '.') {
                // If the last char is a digit any parenthesis but not decimal then append the operator
                _uiState.update { currentState ->
                    currentState.copy(
                        expression = currentState.expression + operator,
                        length = currentState.length + 1
                    )
                }
            }
        }
    }

    fun appendDecimal() {

        val expression = _uiState.value.expression

        if (expression.isEmpty()) {
            // Append decimal for empty expression
            _uiState.update { currentState ->
                currentState.copy(
                    expression = "0.",
                    length = 2
                )
            }
        } else {
            // If expression is not empty check for last character.
            val lastChar = _uiState.value.expression.last()

            if (lastChar != '(') {
                // If last character is not a closing parenthesis then check for existing decimal in the term
                val lastTerm = expression.split(Regex("[-+×÷%(]")).last()
                if (!lastTerm.contains('.')) {
                    _uiState.update { currentState ->
                        currentState.copy(
                            expression = currentState.expression + ".",
                            length = currentState.length + 1
                        )
                    }
                }
            }
        }
    }

    fun appendParenthesis() {
        val expression = _uiState.value.expression

        if (expression.isEmpty()) {
            // If expression is empty then append '('
            _uiState.update { currentState ->
                currentState.copy(
                    expression = "(",
                    length = 1
                )
            }
        } else {
            // If the expression is not empty check of the last character.
            val lastChar = expression.last()

            if (lastChar != '.') {
                // Can't append a parenthesis in case the last char is a decimal

                if (lastChar in listOf('+', '-', '×', '÷', '(')) {
                    // Append opening parenthesis if the last char is + - × ÷ % or (
                    _uiState.update { currentState ->
                        currentState.copy(
                            expression = currentState.expression + "(",
                            length = currentState.length + 1
                        )
                    }
                } else if ((lastChar.isDigit() || lastChar == '%') &&
                    expression.count { it == '(' } > expression.count { it == ')' }
                ) {
                    // If last chara is a digit and there are more opening parenthesis then append a closing parenthesis
                    _uiState.update { currentState ->
                        currentState.copy(
                            expression = currentState.expression + ")",
                            length = currentState.length + 1
                        )
                    }
                }
            }
        }
    }

    fun prepareResult() {
        _uiState.update { currentState ->
            currentState.copy(
                expression = currentState.result,
                length = currentState.result.length,
                result = ""
            )
        }
    }
    fun evaluateExpressionCompact() {
        _uiState.update { currentState ->
            currentState.copy(
                result = Expressions().eval(currentState.expression.replace("×", "*").replace("÷", "/")),
            )
        }
    }

    fun evaluateExpressionExpanded() {
        _uiState.update { currentState ->
            currentState.copy(
                expression = Expressions().eval(currentState.expression.replace("×", "*").replace("÷", "/")),
                length = currentState.result.length
            )
        }
    }

    fun evaluateExpressionMedium() {
        _uiState.update { currentState ->
            currentState.copy(
                expression = Expressions().eval(currentState.expression.replace("×", "*").replace("÷", "/")),
                length = currentState.result.length
            )
        }
    }


}

data class CalculatorUiState(
    val expression: String = "",
    val result: String = "",
    val length: Int = 0
)