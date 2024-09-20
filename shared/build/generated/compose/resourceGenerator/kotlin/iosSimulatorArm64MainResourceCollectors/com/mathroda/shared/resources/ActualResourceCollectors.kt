@file:OptIn(org.jetbrains.compose.resources.InternalResourceApi::class)

package com.mathroda.shared.resources

import kotlin.OptIn
import kotlin.String
import kotlin.collections.Map
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.FontResource
import org.jetbrains.compose.resources.PluralStringResource
import org.jetbrains.compose.resources.StringArrayResource
import org.jetbrains.compose.resources.StringResource

@ExperimentalResourceApi
public actual val Res.allDrawableResources: Map<String, DrawableResource> by lazy {
  val map = mutableMapOf<String, DrawableResource>()
  return@lazy map
}


@ExperimentalResourceApi
public actual val Res.allStringResources: Map<String, StringResource> by lazy {
  val map = mutableMapOf<String, StringResource>()
  return@lazy map
}


@ExperimentalResourceApi
public actual val Res.allStringArrayResources: Map<String, StringArrayResource> by lazy {
  val map = mutableMapOf<String, StringArrayResource>()
  return@lazy map
}


@ExperimentalResourceApi
public actual val Res.allPluralStringResources: Map<String, PluralStringResource> by lazy {
  val map = mutableMapOf<String, PluralStringResource>()
  return@lazy map
}


@ExperimentalResourceApi
public actual val Res.allFontResources: Map<String, FontResource> by lazy {
  val map = mutableMapOf<String, FontResource>()
  return@lazy map
}

