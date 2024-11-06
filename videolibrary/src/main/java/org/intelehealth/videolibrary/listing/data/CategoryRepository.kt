package org.intelehealth.videolibrary.listing.data

import org.intelehealth.videolibrary.model.Category

/**
 * Created by Arpan Sircar. on 08-02-2024.
 * Email : arpan@intelehealth.org
 * Mob   : +919123116015
 **/

class CategoryRepository(private val source: CategoryDataSource) {

    fun fetchAllCategoriesFromServer(auth: String) =
        source.fetchAllCategoriesFromServer(auth)

    suspend fun insertCategories(categories: List<Category>) =
        source.insertCategories(categories)

    fun fetchAllCategoriesFromDb() = source.fetchCategoriesFromDb()

    suspend fun deleteAllCategories() = source.deleteAllCategories()

}