/*******************************************************************************
 * Copyright (c) 2012-2015 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package com.codenvy.ide.extension.maven.server.projecttype;

import com.codenvy.api.project.server.type.ProjectType;
import com.codenvy.ide.ext.java.server.projecttype.JavaProjectType;
import com.codenvy.ide.extension.maven.shared.MavenAttributes;
import com.google.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

/** @author Evgen Vidolob */
@Singleton
public class MavenProjectType extends ProjectType {
    private static final Logger LOG = LoggerFactory.getLogger(MavenProjectType.class);

    @Inject
    public MavenProjectType(MavenValueProviderFactory mavenValueProviderFactory,
                            JavaProjectType javaProjectType) {

        super(MavenAttributes.MAVEN_ID, MavenAttributes.MAVEN_NAME, true, false);
        addVariableDefinition(MavenAttributes.GROUP_ID, "", false, mavenValueProviderFactory);
        addVariableDefinition(MavenAttributes.ARTIFACT_ID, "", true, mavenValueProviderFactory);
        addVariableDefinition(MavenAttributes.VERSION, "", false, mavenValueProviderFactory);
        addVariableDefinition(MavenAttributes.PARENT_VERSION, "", false, mavenValueProviderFactory);
        addVariableDefinition(MavenAttributes.PARENT_ARTIFACT_ID, "", false, mavenValueProviderFactory);
        addVariableDefinition(MavenAttributes.PARENT_GROUP_ID, "", false, mavenValueProviderFactory);
        addVariableDefinition(MavenAttributes.PACKAGING, "", false, mavenValueProviderFactory);
        addVariableDefinition(MavenAttributes.SOURCE_FOLDER, "", false, mavenValueProviderFactory);
        addVariableDefinition(MavenAttributes.TEST_SOURCE_FOLDER, "", false, mavenValueProviderFactory);

        addParent(javaProjectType);
        setDefaultBuilder("maven");
    }

}