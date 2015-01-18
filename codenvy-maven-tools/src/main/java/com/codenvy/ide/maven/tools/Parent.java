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
package com.codenvy.ide.maven.tools;

import com.codenvy.commons.xml.Element;
import com.codenvy.commons.xml.NewElement;

import static com.codenvy.commons.xml.NewElement.createElement;
import static com.codenvy.commons.xml.XMLTreeLocation.after;
import static com.codenvy.commons.xml.XMLTreeLocation.before;
import static com.codenvy.commons.xml.XMLTreeLocation.inTheBegin;
import static com.codenvy.commons.xml.XMLTreeLocation.inTheEnd;

/**
 * The {@literal <parent>} element contains information required to
 * locate the parent project which this project will inherit from.
 * <p/>
 * Supports next data:
 * <ul>
 * <li>artifactId</li>
 * <li>groupId</li>
 * <li>version</li>
 * <li>relativePath</li>
 * </ul>
 *
 * @author Eugene Voevodin
 */
public class Parent {

    private String groupId;
    private String artifactId;
    private String version;
    private String relativePath;

    Element parentElement;

    public Parent() {
    }

    public Parent(String groupId, String artifactId, String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    Parent(Element element) {
        parentElement = element;
        groupId = element.getChildText("groupId");
        artifactId = element.getChildText("artifactId");
        version = element.getChildText("version");
    }

    /**
     * Returns the artifact id of the parent project to inherit from.
     */
    public String getArtifactId() {
        return artifactId;
    }

    /**
     * Returns the group id of the parent project to inherit from
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * Returns the version of the parent project to inherit
     */
    public String getVersion() {
        return version;
    }

    public String getRelativePath() {
        return relativePath == null ? "../pom.xml" : relativePath;
    }

    /**
     * Sets the artifact id of the parent project to inherit from
     */
    public Parent setArtifactId(String artifactId) {
        this.artifactId = artifactId;
        if (!isNew()) {
            if (artifactId == null) {
                parentElement.removeChild("artifactId");
            } else if (parentElement.hasSingleChild("artifactId")) {
                parentElement.getSingleChild("artifactId").setText(artifactId);
            } else {
                parentElement.insertChild(createElement("artifactId", artifactId), after("groupId").or(inTheBegin()));
            }
        }
        return this;
    }

    /**
     * Sets the group id of the parent project to inherit from
     */
    public Parent setGroupId(String groupId) {
        this.groupId = groupId;
        if (!isNew()) {
            if (groupId == null) {
                parentElement.removeChild("groupId");
            } else if (parentElement.hasSingleChild("groupId")) {
                parentElement.getSingleChild("groupId").setText(groupId);
            } else {
                parentElement.insertChild(createElement("groupId", groupId), inTheBegin());
            }
        }
        return this;
    }

    /**
     * Sets the version of the parent project to inherit
     */
    public Parent setVersion(String version) {
        this.version = version;
        if (!isNew()) {
            if (version == null) {
                parentElement.removeChild("version");
            } else if (parentElement.hasSingleChild("version")) {
                parentElement.getSingleChild("version").setText(version);
            } else {
                parentElement.insertChild(createElement("version", version), before("relativePath").or(inTheEnd()));
            }
        }
        return this;
    }

    /**
     * Sets parent relative path
     */
    public Parent setRelativePath(String relativePath) {
        this.relativePath = relativePath;
        if (!isNew()) {
            if (relativePath == null) {
                parentElement.removeChild("relativePath");
            } else if (parentElement.hasSingleChild("relativePath")) {
                parentElement.getSingleChild("relativePath").setText(relativePath);
            } else {
                parentElement.appendChild(createElement("relativePath", relativePath));
            }
        }
        return this;
    }

    /**
     * Returns the id as <i>groupId:artifactId:version</i>
     */
    public String getId() {
        return groupId + ':' + artifactId + ":pom:" + version;
    }

    @Override
    public String toString() {
        return getId();
    }

    NewElement asXMLElement() {
        final NewElement newParent = createElement("parent");
        if (groupId != null) {
            newParent.appendChild(createElement("groupId", groupId));
        }
        if (artifactId != null) {
            newParent.appendChild(createElement("artifactId", artifactId));
        }
        if (version != null) {
            newParent.appendChild(createElement("version", version));
        }
        if (relativePath != null) {
            newParent.appendChild(createElement("relativePath", relativePath));
        }
        return newParent;
    }

    private boolean isNew() {
        return parentElement == null;
    }
}
