package de.kitshn.android.model.form.item.field

import android.content.Context
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import de.kitshn.android.R
import de.kitshn.android.api.tandoor.TandoorClient
import de.kitshn.android.ui.component.input.recipe.RecipeSearchField

class KitshnFormRecipeSearchFieldItem(
    val client: TandoorClient,
    val value: () -> Int?,
    val onValueChange: (value: Int?) -> Unit,

    label: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,

    optional: Boolean = false,

    val check: (data: Int?) -> String?
) : KitshnFormBaseFieldItem(
    label = label,
    leadingIcon = leadingIcon,
    trailingIcon = trailingIcon,
    placeholder = placeholder,
    prefix = prefix,
    suffix = suffix,
    optional = optional
) {

    var context: Context? = null

    @Composable
    override fun Render() {
        val focusManager = LocalFocusManager.current

        var error by rememberSaveable { mutableStateOf<String?>(null) }
        val value = value()

        context = LocalContext.current

        RecipeSearchField(
            modifier = Modifier.fillMaxWidth(),
            client = client,
            value = value,
            label = {
                Row {
                    label()
                    if(!optional) Text("*")
                }
            },

            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            placeholder = placeholder,
            prefix = prefix,
            suffix = suffix,

            isError = error != null || generalError != null,
            supportingText = if(error != null || generalError != null) {
                {
                    Text(
                        text = error ?: generalError ?: "",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            } else null,

            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),

            onValueChange = {
                generalError = null
                onValueChange(it)

                error = if(it == null)
                    null
                else
                    check(it)
            }
        )
    }

    override fun submit(): Boolean {
        val value = value()
        val checkResult = check(value)

        if(!optional && value == null) {
            generalError = context?.getString(R.string.form_error_field_empty)
            return false
        } else if(checkResult != null) {
            generalError = checkResult
            return false
        } else {
            generalError = null
            return true
        }
    }

}