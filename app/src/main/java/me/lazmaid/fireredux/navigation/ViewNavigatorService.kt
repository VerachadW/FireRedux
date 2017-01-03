package me.lazmaid.fireredux.navigation

/**
 * Created by VerachadW on 1/4/2017 AD.
 */

interface ViewNavigatorService {
    fun navigateTo(viewKey: ViewKey)
    fun back()
}