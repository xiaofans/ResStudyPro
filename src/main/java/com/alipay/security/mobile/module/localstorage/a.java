package com.alipay.security.mobile.module.localstorage;

import android.os.Environment;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public final class a {
    public static String a(String str) {
        String str2 = "";
        try {
            str2 = System.getProperty(str);
        } catch (Throwable th) {
        }
        return com.alipay.security.mobile.module.localstorage.util.a.a(str2) ? b.a(".SystemConfig" + File.separator + str) : str2;
    }

    public static void a(String str, String str2) {
        FileWriter fileWriter;
        Throwable th;
        try {
            if (!com.alipay.security.mobile.module.localstorage.util.a.a(str2)) {
                System.setProperty(str, str2);
            }
        } catch (Throwable th2) {
        }
        if (b.a()) {
            String str3 = ".SystemConfig" + File.separator + str;
            try {
                if (b.a()) {
                    File file = new File(Environment.getExternalStorageDirectory(), str3);
                    if (!file.exists()) {
                        file.getParentFile().mkdirs();
                    }
                    File file2 = new File(file.getAbsolutePath());
                    FileWriter fileWriter2 = null;
                    try {
                        fileWriter = new FileWriter(file2, false);
                        try {
                            fileWriter.write(str2);
                            try {
                                fileWriter.close();
                            } catch (IOException e) {
                            }
                        } catch (Exception e2) {
                            if (fileWriter != null) {
                                try {
                                    fileWriter.close();
                                } catch (IOException e3) {
                                }
                            }
                        } catch (Throwable th3) {
                            Throwable th4 = th3;
                            fileWriter2 = fileWriter;
                            th = th4;
                            if (fileWriter2 != null) {
                                try {
                                    fileWriter2.close();
                                } catch (IOException e4) {
                                }
                            }
                            throw th;
                        }
                    } catch (Exception e5) {
                        fileWriter = null;
                        if (fileWriter != null) {
                            fileWriter.close();
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        if (fileWriter2 != null) {
                            fileWriter2.close();
                        }
                        throw th;
                    }
                }
            } catch (Exception e6) {
            }
        }
    }
}
