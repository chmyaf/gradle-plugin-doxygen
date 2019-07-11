/*
 * Copyright 2019 Andrey S Teplitskiy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chmyaf.gradle.plugin.doxygen;

import java.util.ArrayList;

/**
 * \brief Doxygen plugin parameters.
 *
 * Plugin options and doxygen settings.
 */
public class DoxygenPluginExtension {
    /// Path to the Doxygen binary.
    public String bin = "";
    /// Path to custom Doxyfile.
    public String doxyfile = "";
    /// Path to generated Doxyfile.
    public String doxyfileDefault = "";
    /// Doxyfile: EXTRACT_ANON_METHODS.
    public boolean extractAnonMethods = true;
    /// Doxyfile: EXTRACT_LOCAL_CLASSES.
    public boolean extractLocalClasses = true;
    /// Doxyfile: EXTRACT_LOCAL_METHODS.
    public boolean extractLocalMethods = true;
    /// Doxyfile: EXTRACT_PACKAGE.
    public boolean extractPackage = true;
    /// Doxyfile: EXTRACT_PRIVATE.
    public boolean extractPrivate = true;
    /// Doxyfile: EXTRACT_STATIC.
    public boolean extractStatic = true;
    /// Doxyfile: GENERATE_LATEX.
    public boolean generateLatex = false;
    /// Doxyfile: INPUT
    public ArrayList<String> input = new ArrayList<>();
    /// Doxyfile: JAVADOC_AUTOBRIEF.
    public boolean javadocAutobrief = false;
    /// Doxyfile: OUTPUT_DIRECTORY
    public String outputDirectory = "";
    /// Doxyfile: PROJECT_NAME.
    public String projectName = "";
    /// Doxyfile: PROJECT_NUMBER.
    public String projectNumber = "";
    /// Doxyfile: RECURSIVE.
    public boolean recursive = true;
    /// Doxyfile: QUIET.
    public boolean quiet = true;
    /// Doxyfile: WARN_NO_PARAMDOC.
    public boolean warnNoParamDoc = true;
}
