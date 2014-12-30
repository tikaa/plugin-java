/*******************************************************************************
 * Copyright (c) 2012-2014 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/

package com.codenvy.ide.ext.java;


import com.codenvy.ide.ext.java.server.JsonSearchRequester;

import org.fest.assertions.Assertions;
import org.junit.Test;

/**
 * @author Evgen Vidolob
 */
public class SearchableEnvironmentTest extends BaseTest{

    @Test
    public void testAccessRule() throws Exception {
        JsonSearchRequester storage = new JsonSearchRequester();
        project.getNameEnvironment().findTypes("st".toCharArray(), true, true, 0, storage);
        Assertions.assertThat(storage.toJsonString()).doesNotContain("com.sun.xml.internal.stream.buffer.stax.StreamWriterBufferProcessor")
                                                     .doesNotContain("sun.font.StandardGlyphVector")
                                                     .doesNotContain("sun.nio.cs.StreamDecoder ");
    }
}