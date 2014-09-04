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
package com.codenvy.ide.ext.java.client.projecttree;

import com.codenvy.api.project.gwt.client.ProjectServiceClient;
import com.codenvy.api.project.shared.dto.ItemReference;
import com.codenvy.ide.api.editor.EditorAgent;
import com.codenvy.ide.api.icon.IconRegistry;
import com.codenvy.ide.api.projecttree.AbstractTreeNode;
import com.codenvy.ide.api.projecttree.TreeSettings;
import com.codenvy.ide.api.projecttree.generic.FolderNode;
import com.codenvy.ide.rest.DtoUnmarshallerFactory;
import com.google.web.bindery.event.shared.EventBus;

/**
 * Node that represents a java package.
 *
 * @author Artem Zatsarynnyy
 */
public class PackageNode extends FolderNode {

    public PackageNode(AbstractTreeNode parent, ItemReference data, JavaTreeStructure treeStructure, TreeSettings settings,
                       EventBus eventBus, EditorAgent editorAgent, ProjectServiceClient projectServiceClient,
                       DtoUnmarshallerFactory dtoUnmarshallerFactory, IconRegistry iconRegistry) {
        super(parent, data, treeStructure, settings, eventBus, editorAgent, projectServiceClient, dtoUnmarshallerFactory);

        getPresentation().setSvgIcon(iconRegistry.getIcon("java.package").getSVGImage());
    }

    /**
     * Returns the full-qualified name of the package.
     *
     * @return the full-qualified name, or an empty string for the default package
     */
    public String getQualifiedName() {
        // TODO: read source folders from project/module attributes
        final String p = getProject().getPath() + "/src/main/java/";
        return getPath().replaceFirst(p, "").replaceAll("/", ".");
    }

    /** {@inheritDoc} */
    @Override
    protected AbstractTreeNode<?> createNode(ItemReference item) {
        if (isFile(item) && item.getName().endsWith(".java")) {
            return ((JavaTreeStructure)treeStructure).newSourceFileNode(this, item);
        } else if (isFolder(item)) {
            return ((JavaTreeStructure)treeStructure).newPackageNode(this, item);
        } else {
            return super.createNode(item);
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean isRenemable() {
        // Do not allow to rename Java package as simple folder.
        // This type of node needs to implement rename refactoring.
        return false;
    }
}