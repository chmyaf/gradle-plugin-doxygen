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

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleScriptException;
import org.gradle.api.tasks.TaskAction;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DoxygenTask extends DefaultTask {
    private DoxygenPluginExtension dpe;
    private HashMap<String, String> params = new HashMap<>();

    public DoxygenTask() {
        this.dpe = this.getProject().getExtensions().
                findByType(DoxygenPluginExtension.class);
    }

    @TaskAction
    public void apply() {
        boolean isChangedConfiguration = false;

        this.collectParams();

        this.mkdirs();

        this.writeParamsTemp();

        if (this.isParamsInTaskWasUpdated()) {
            this.updateParams();
            isChangedConfiguration = true;
        }

        if (this.getDoxyFile().getPath().isEmpty()) {
            this.genDoxyFileDefault();
        }

        if (!this.getDoxyFile().exists()) {
            throw new GradleScriptException("Doxyfile is not exists: " +
                    this.getDoxyFile().getAbsolutePath(),
                    new FileNotFoundException()
            );
        }

        if (isDoxyFileWasUpdated()) {
            isChangedConfiguration = true;
        }

        if (isChangedConfiguration) {
            this.genDoxyFileBuild();
        }

        this.runDoxygenGeneration();
    }

    private void collectParams() {
        StringBuilder input = new StringBuilder();
        for (String src : this.getDPE().input) {
            input.append(src);
            input.append(" \\");
            input.append(System.lineSeparator());
        }
        input.append(System.lineSeparator());
        this.params.put("INPUT", input.toString());
        this.params.put("GENERATE_LATEX",
                this.getDPE().generateLatex ? "YES" : "NO"
        );
        this.params.put("OUTPUT_DIRECTORY", this.getDPE().outputDirectory);
        this.params.put("PROJECT_NAME", this.getProjectName());
        this.params.put("PROJECT_NUMBER", this.getProjectNumber());
        this.params.put("QUIET", this.getDPE().quiet ? "YES" : "NO");
        this.params.put("WARN_NO_PARAMDOC",
                this.getDPE().warnNoParamDoc ? "YES" : "NO"
        );
    }

    private void genDoxyFileBuild() {
        FileReader fr;
        FileWriter fw;
        BufferedReader br;
        BufferedWriter bw;
        String str;
        try {
            fr = new FileReader(this.getDoxyFile());
        } catch (FileNotFoundException e) {
            throw new GradleScriptException("Can't open src doxyfile: " +
                    this.getDoxyFile().getAbsolutePath(),
                    new Exception(e)
            );
        }

        try {
            fw = new FileWriter(this.getDoxyFileBuild());
        } catch (IOException e) {
            throw new GradleScriptException("Can't open dst doxyfile: " +
                    this.getDoxyFileBuild().getAbsolutePath(),
                    new Exception(e)
            );
        }

        br = new BufferedReader(fr);
        bw = new BufferedWriter(fw);

        try {
            while ((str = br.readLine()) != null) {
                if ((str.equals("")) || (str.matches("^([\\s+]?)#.*"))) {
                    continue;
                }
                str = this.genDoxyFileBuildRow(str);
                str += System.lineSeparator();
                try {
                    bw.write(str);
                } catch (IOException e) {
                    throw new GradleScriptException("Can't write doxyfile: " +
                            this.getDoxyFileBuild().getAbsolutePath(),
                            new IOException(e)
                    );
                }
            }
        } catch (IOException e) {
            throw new GradleScriptException("Can't read doxyfile: " +
                    this.getDoxyFile().getAbsolutePath(),
                    new IOException(e)
            );
        }

        try {
            br.close();
        } catch (IOException e) {
            throw new GradleScriptException("Can't close readed doxyfile:" +
                    this.getDoxyFile().getAbsolutePath(),
                    new IOException(e)
            );
        }

        try {
            bw.flush();
            bw.close();
        } catch (IOException e) {
            throw new GradleScriptException("Can't close writed doxyfile:" +
                    this.getDoxyFile().getAbsolutePath(),
                    new IOException(e)
            );
        }
    }

    private String genDoxyFileBuildRow(String str) {
        String param;
        for (Map.Entry<String, String> entry : this.params.entrySet()) {
            param = entry.getKey();
            if (str.matches("^((\\s*)?)" + param + "((\\s*)?)=(.*)")) {
                str = param + " = " + entry.getValue();
                break;
            }
        }
        return str;
    }

    private void genDoxyFileDefault() {
        this.getDPE().doxyfile = this.getDoxyFileDefault().getAbsolutePath();
        if (!this.getDoxyFile().exists()) {
            this.getDPE().doxyfile = new File(this.getTempDir(),
                    "Doxyfile.autogenerated").getAbsolutePath();
            if (!this.getDoxyFile().exists()) {
                this.runDoxygenDoxyFileGeneration(this.getDoxyFile());
            }
        }
    }

    private String getCheckSum(File fl) throws FileNotFoundException {
        String result;
        String algorithm = "MD5";
        InputStream is;
        MessageDigest md;
        byte[] buff = new byte[1024];
        int c;

        try {
            is = new FileInputStream(fl);
            md = MessageDigest.getInstance(algorithm);
            try {
                while ((c = is.read(buff)) > 0) {
                    md.update(buff, 0, c);
                }
                result = new BigInteger(1, md.digest()).toString(16);
                if (result.length() == 31) {
                    result = "0" + result;
                }
            } catch (IOException e) {
                throw new GradleScriptException(
                        "Can't read " + fl.getAbsolutePath(),
                        new IOException(e)
                );
            }
        } catch (NoSuchAlgorithmException e) {
            throw new GradleScriptException(
                    "Can't find alghoritm " + algorithm,
                    new NoSuchAlgorithmException(e)
            );
        }

        return result;
    }

    private String getBin() {
        return this.getDPE().bin;
    }

    private DoxygenPluginExtension getDPE() {
        return this.dpe;
    }

    private File getDoxyFile() {
        return new File(this.getDPE().doxyfile);
    }

    private File getDoxyFileDefault() {
        return new File(this.getDPE().doxyfileDefault);
    }

    private File getDoxyFileBuild() {
        return new File(this.getTempDir(), "Doxyfile");
    }

    private File getParamsFile() {
        return new File(this.getTempDir(), "params");
    }

    private File getParamsTempFile() {
        return new File(this.getTempDir(), "params.tmp");
    }

    private String getProjectName() {
        return this.dpe.projectName;
    }

    private String getProjectNumber() {
        String result = this.getDPE().projectNumber;

        if (result.isEmpty()) {
            result = this.getProject().getVersion().toString();
        }

        return result;
    }

    private File getTempDir() {
        return new File(
                new File(this.getProject().getBuildDir(), "tmp"), "doxygen"
        );
    }

    private boolean isDoxyFileWasUpdated() {
        return this.isFileWasUpdated(this.getDoxyFile(), this.getDoxyFileBuild());
    }

    private boolean isFileWasUpdated(File src, File dst) {
        boolean result = false;
        String checksumDst;
        String checksumSrc;

        try {
            checksumDst = this.getCheckSum(dst);
        } catch (FileNotFoundException e) {
            checksumDst = null;
        }

        try {
            checksumSrc = this.getCheckSum(src);
        } catch (FileNotFoundException e) {
            throw new GradleScriptException(
                    "Can't get " + src.getAbsolutePath(),
                    new Exception(e)
            );
        }

        if ((checksumDst == null) ||
                (!checksumDst.equals(checksumSrc))
        ) {
            result = true;
        }

        return result;
    }

    private boolean isParamsInTaskWasUpdated() {
        return this.isFileWasUpdated(this.getParamsTempFile(),
                this.getParamsFile()
        );
    }

    private void mkdirs() {
        this.getTempDir().mkdirs();
        new File(this.getDPE().outputDirectory).mkdirs();
    }

    private void runDoxygen(ArrayList<String> args) {
        this.getProject().exec(execSpec -> {
            execSpec.executable(this.getBin());
            for (String param : args) {
                execSpec.args(param);
            }
        });
    }

    private void runDoxygenDoxyFileGeneration(File path) {
        ArrayList<String> params = new ArrayList<>();
        params.add("-g");
        params.add(path.getAbsolutePath());
        this.runDoxygen(params);
    }

    private void runDoxygenGeneration() {
        ArrayList<String> params = new ArrayList<>();
        params.add(this.getDoxyFileBuild().getAbsolutePath());
        this.runDoxygen(params);
    }

    private void updateFile(File src, File dst) {
        try {
            Files.copy(src.toPath(),
                    dst.toPath(),
                    StandardCopyOption.REPLACE_EXISTING
            );
        } catch (IOException e) {
            throw new GradleScriptException("Can't copy " +
                    src.getAbsolutePath() +
                    " to " +
                    dst.getAbsolutePath(),
                    new IOException(e)
            );
        }
    }

    private void updateParams() {
        this.updateFile(this.getParamsTempFile(), this.getParamsFile());
    }

    private void writeParamsTemp() {
        File fl;
        FileWriter fw;
        try {
            fl = this.getParamsTempFile();
            fw = new FileWriter(fl);

            BufferedWriter bw = new BufferedWriter(fw);
            for (Map.Entry<String, String> entry : this.params.entrySet()) {
                bw.write(entry.getKey() + "=" + entry.getValue() +
                        System.lineSeparator()
                );
            }

            bw.flush();
            bw.close();
        } catch (IOException e) {
            throw new GradleScriptException("Can't write doxygen params",
                    new Exception(e)
            );
        }
    }
}
