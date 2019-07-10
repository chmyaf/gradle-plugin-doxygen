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

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.SourceSet;

import java.io.File;

/**
 * The Doxygen plugin.
 */
public class DoxygenPlugin implements Plugin<Project> {
    /**
     * Apply plugin method for the Gradle.
     *
     * @param project Project parameters from the Gradle.
     */
    @Override
    public void apply(Project project) {
        DoxygenPluginExtension dpe;
        DoxygenTask taskDoxygen;
        SourceSet mainSourceSet;

        dpe = project.getExtensions().
                create("doxygen", DoxygenPluginExtension.class);
        dpe.bin = "doxygen";
        dpe.doxyfileDefault = new File(
                project.getProjectDir().getAbsolutePath(),
                "Doxyfile").getAbsolutePath();
        dpe.outputDirectory = new File(
                new File(project.getBuildDir(), "docs"),
                "doxygen").getAbsolutePath();
        System.out.println(dpe.outputDirectory);
        dpe.projectName = project.getName();

        try {
            mainSourceSet = project.getConvention().
                    getPlugin(JavaPluginConvention.class).
                    getSourceSets().
                    getByName("main");
            for (File fl : mainSourceSet.getAllSource().getSrcDirs()) {
                dpe.input.add(fl.getAbsolutePath());
            }
        } catch (Exception ignored) {
        }

        taskDoxygen = project.getTasks().create("doxygen", DoxygenTask.class);
        taskDoxygen.setGroup("documentation");
        taskDoxygen.setDescription("Generates Doxygen API documentation.");
    }
}
