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
package com.codenvy.ide.ext.java.client.editor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.vectomatic.dom.svg.ui.SVGImage;

import com.codenvy.ide.api.text.annotation.Annotation;
import com.codenvy.ide.api.texteditor.quickassist.QuickFixableAnnotation;
import com.codenvy.ide.ext.java.client.JavaResources;
import com.codenvy.ide.ext.java.jdt.core.compiler.CategorizedProblem;
import com.codenvy.ide.ext.java.jdt.core.compiler.IProblem;
import com.codenvy.ide.runtime.Assert;
import com.codenvy.ide.util.dom.Elements;
import com.google.gwt.user.client.ui.Image;

import elemental.dom.Element;
import elemental.js.dom.JsElement;

/** Annotation representing an <code>IProblem</code>. */
public class ProblemAnnotation extends Annotation implements JavaAnnotation, QuickFixableAnnotation {

    public static final String ERROR_ANNOTATION_TYPE = "org.eclipse.jdt.ui.error"; //$NON-NLS-1$
    public static final String WARNING_ANNOTATION_TYPE = "org.eclipse.jdt.ui.warning"; //$NON-NLS-1$
    public static final String INFO_ANNOTATION_TYPE = "org.eclipse.jdt.ui.info"; //$NON-NLS-1$
    public static final String TASK_ANNOTATION_TYPE = "org.eclipse.ui.workbench.texteditor.task"; //$NON-NLS-1$
    /** The layer in which task problem annotations are located. */
    private static final int TASK_LAYER;
    /** The layer in which info problem annotations are located. */
    private static final int INFO_LAYER;
    /** The layer in which warning problem annotations representing are located. */
    private static final int WARNING_LAYER;
    /** The layer in which error problem annotations representing are located. */
    private static final int ERROR_LAYER;
    /** The resources in which image and CSS links are located. */
    private static final JavaResources javaRes;

    static {
        // TODO configure this
        TASK_LAYER = 0;
        INFO_LAYER = 1;
        WARNING_LAYER = 2;
        ERROR_LAYER = 3;
        javaRes = JavaResources.INSTANCE;
        javaRes.css().ensureInjected();
    }

    private static JsElement fgQuickFixElement = (new SVGImage(javaRes.markWarning())).getElement().cast();
    private static JsElement fgQuickFixErrorElement = (new SVGImage(javaRes.markError())).getElement().cast();
    private static JsElement fgTaskElement = (new Image(javaRes.taskmrk())).getElement().cast();
    private static JsElement fgInfoElement = (new Image(javaRes.imp_obj())).getElement().cast();
    private static JsElement fgWarningElement = (new SVGImage(javaRes.markWarning())).getElement().cast();
    private static JsElement fgErrorElement = (new SVGImage(javaRes.markError())).getElement().cast();
    private List<JavaAnnotation> fOverlaids;
    private IProblem fProblem;
    private Element fImageElement = null;
    private int fLayer = 0;
    private boolean fIsQuickFixable;
    private boolean fIsQuickFixableStateSet = false;

    public ProblemAnnotation(IProblem problem) {

        fProblem = problem;

        if (IProblem.Task == fProblem.getID()) {
            setType(TASK_ANNOTATION_TYPE);
            fLayer = TASK_LAYER;
        } else if (fProblem.isWarning()) {
            setType(WARNING_ANNOTATION_TYPE);
            fLayer = WARNING_LAYER;
        } else if (fProblem.isError()) {
            setType(ERROR_ANNOTATION_TYPE);
            fLayer = ERROR_LAYER;
        } else {
            setType(INFO_ANNOTATION_TYPE);
            fLayer = INFO_LAYER;
        }
    }

    /** @return */
    public int getLayer() {
        return fLayer;
    }

    private void initializeImage() {

        fImageElement = Elements.createDivElement();
        fImageElement.setClassName(javaRes.css().markElement());

        final Element selectedImageElement = getSelectedImageElement();
        if (selectedImageElement != null) {
            fImageElement.setInnerHTML(selectedImageElement.getOuterHTML());
        }
    }

    private Element getSelectedImageElement() {
        if (!isQuickFixableStateSet()) {
            setQuickFixable(isProblem() && JavaAnnotationUtil.hasCorrections(this)); // no light bulb for tasks
        }
        final String type = getType();
        if (isQuickFixable()) {
            switch (type) {
                case ERROR_ANNOTATION_TYPE:
                    return fgQuickFixErrorElement;
                default:
                    return fgQuickFixElement;
            }
        } else {
            switch (type) {
                case TASK_ANNOTATION_TYPE:
                    return fgTaskElement;
                case INFO_ANNOTATION_TYPE:
                    return fgInfoElement;
                case WARNING_ANNOTATION_TYPE:
                    return fgWarningElement;
                case ERROR_ANNOTATION_TYPE:
                    return fgErrorElement;
                default:
                    return null;
            }
        }
    }

    @Override
    public String getText() {
        return fProblem.getMessage();
    }

    @Override
    public String[] getArguments() {
        return isProblem() ? fProblem.getArguments() : null;
    }

    @Override
    public int getId() {
        return fProblem.getID();
    }

    @Override
    public boolean isProblem() {
        String type = getType();
        return WARNING_ANNOTATION_TYPE.equals(type) || ERROR_ANNOTATION_TYPE.equals(type);
    }

    @Override
    public boolean hasOverlay() {
        return false;
    }

    @Override
    public JavaAnnotation getOverlay() {
        return null;
    }

    @Override
    public void addOverlaid(JavaAnnotation annotation) {
        if (fOverlaids == null) {
            fOverlaids = new ArrayList<JavaAnnotation>(1);
        }
        fOverlaids.add(annotation);
    }

    @Override
    public void removeOverlaid(JavaAnnotation annotation) {
        if (fOverlaids != null) {
            fOverlaids.remove(annotation);
            if (fOverlaids.size() == 0) {
                fOverlaids = null;
            }
        }
    }

    @Override
    public Iterator<JavaAnnotation> getOverlaidIterator() {
        if (fOverlaids != null) {
            return fOverlaids.iterator();
        }
        return null;
    }

    @Override
    public String getMarkerType() {
        if (fProblem instanceof CategorizedProblem) {
            return ((CategorizedProblem)fProblem).getMarkerType();
        }
        return null;
    }

    @Override
    public boolean isQuickFixableStateSet() {
        return fIsQuickFixableStateSet;
    }

    @Override
    public boolean isQuickFixable() {
        Assert.isTrue(isQuickFixableStateSet());
        return fIsQuickFixable;
    }

    @Override
    public void setQuickFixable(boolean state) {
        fIsQuickFixable = state;
        fIsQuickFixableStateSet = true;
    }

    @Override
    public Element getImageElement() {
        if (fImageElement == null) {
            initializeImage();
        }
        return fImageElement;
    }
}
