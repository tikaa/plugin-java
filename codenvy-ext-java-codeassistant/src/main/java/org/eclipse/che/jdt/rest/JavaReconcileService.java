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

package org.eclipse.che.jdt.rest;

import com.google.inject.Inject;

import org.eclipse.che.ide.ext.java.shared.dto.ReconcileResult;
import org.eclipse.che.jdt.JavaProjectService;
import org.eclipse.che.jdt.internal.core.JavaProject;
import org.eclipse.che.jdt.javaeditor.JavaReconciler;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 * @author Evgen Vidolob
 */
@Path("reconcile/{ws-id}")
public class JavaReconcileService {


    @Inject
    private JavaProjectService projectService;

    @Inject
    private JavaReconciler reconciler;

    @PathParam("ws-id")
    private String wsId;

    @GET
    @Produces("application/json")
    public ReconcileResult reconcile(@QueryParam("projectpath") String projectPath, @QueryParam("fqn") String fqn) {
        JavaProject javaProject = projectService.getOrCreateJavaProject(wsId, projectPath);
        return reconciler.reconcile(javaProject, fqn);
    }
}
