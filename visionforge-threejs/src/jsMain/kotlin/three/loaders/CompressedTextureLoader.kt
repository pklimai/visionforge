/*
 * The MIT License
 *
 * Copyright 2017-2018 Lars Ivar Hatledal
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING  FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

@file:JsModule("three")


package three.loaders

import org.w3c.xhr.XMLHttpRequest
import three.textures.Texture

/**
 * Abstract base class for block based textures loader (dds, pvr, ...).
 * This uses the FileLoader internally for loading files.
 *
 * @param manager The loadingManager for the loader to use. Default is THREE.DefaultLoadingManager.
 */
external class CompressedTextureLoader(
    manager: LoadingManager = definedExternally
) {

    /**
     * The loadingManager the loader is using. Default is DefaultLoadingManager.
     */
    var manager: LoadingManager
    /**
     * The base path from which files will be loaded. See .setPath. Default is undefined.
     */
    var path: String

    /**
     * Begin loading from url and pass the loaded texture to onLoad.
     *
     * @param url the path or URL to the file. This can also be a Data URI.
     * @param onLoad Will be called when load completes. The argument will be the loaded texture.
     * @param onProgress Will be called while load progresses. The argument will be the XMLHttpRequest instance, which contains .total and .loaded bytes.
     * @param onError Will be called when load errors.
     */
    fun load(
        url: String,
        onLoad: (Texture) -> Unit,
        onProgress: (XMLHttpRequest) -> Unit = definedExternally,
        onError: (dynamic) -> Unit = definedExternally
    )

    /**
     * Set the base path or URL from which to load files.
     * This can be useful if you are loading many textures from the same directory.
     */
    fun setPath(path: String)

}