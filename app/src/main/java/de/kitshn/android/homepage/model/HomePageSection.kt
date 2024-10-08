package de.kitshn.android.homepage.model

import de.kitshn.android.api.tandoor.route.TandoorRecipeQueryParameters
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class HomePageSection(
    val title: Int,
    val queryParameters: List<TandoorRecipeQueryParameters>
) {

    @Transient
    val loading: Boolean = false

    @Transient
    val recipeIds = mutableListOf<Int>()

}