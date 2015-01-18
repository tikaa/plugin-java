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
package com.codenvy.ide.extension.maven.client;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Describes the Maven archetype.
 *
 * @author Artem Zatsarynnyy
 */
public class MavenArchetype {
    private final String groupId;
    private final String artifactId;
    private final String version;
    private final String repository;

    /**
     * Create archetype description with the specified common properties' values.
     * <p/>
     * External repository may be specified to search for an archetype that are not available on Maven central repository.
     *
     * @param groupId
     *         the archetype's groupId
     * @param artifactId
     *         the archetype's artifactId
     * @param version
     *         the archetype's version
     * @param repository
     *         the repository where need to find the archetype
     */
    public MavenArchetype(@Nonnull String groupId, @Nonnull String artifactId, @Nonnull String version, @Nullable String repository) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.repository = repository;
    }

    @Nonnull
    public String getGroupId() {
        return groupId;
    }

    @Nonnull
    public String getArtifactId() {
        return artifactId;
    }

    @Nonnull
    public String getVersion() {
        return version;
    }

    @Nullable
    public String getRepository() {
        return repository;
    }

    @Override
    public String toString() {
        return groupId + ':' + artifactId + ':' + version;
    }
}
