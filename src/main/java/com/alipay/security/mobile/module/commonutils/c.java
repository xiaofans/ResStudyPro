package com.alipay.security.mobile.module.commonutils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public final class c {
    public static String a(String str, String str2) {
        FileReader fileReader;
        Exception e;
        Throwable th;
        try {
            File file = new File(str, str2);
            if (file.exists()) {
                char[] cArr = new char[((int) file.length())];
                fileReader = new FileReader(file);
                try {
                    fileReader.read(cArr);
                    String valueOf = String.valueOf(cArr);
                    try {
                        fileReader.close();
                        return valueOf;
                    } catch (IOException e2) {
                        e2.printStackTrace();
                        return valueOf;
                    }
                } catch (Exception e3) {
                    e = e3;
                    try {
                        e.getMessage();
                        try {
                            fileReader.close();
                        } catch (IOException e4) {
                            e4.printStackTrace();
                        }
                        return "";
                    } catch (Throwable th2) {
                        th = th2;
                        try {
                            fileReader.close();
                        } catch (IOException e22) {
                            e22.printStackTrace();
                        }
                        throw th;
                    }
                }
            }
            fileReader = null;
            try {
                fileReader.close();
                return null;
            } catch (IOException e222) {
                e222.printStackTrace();
                return null;
            }
        } catch (Exception e5) {
            Exception exception = e5;
            fileReader = null;
            e = exception;
            e.getMessage();
            fileReader.close();
            return "";
        } catch (Throwable th3) {
            Throwable th4 = th3;
            fileReader = null;
            th = th4;
            fileReader.close();
            throw th;
        }
    }

    private static boolean a(String str) {
        if (str != null) {
            int length = str.length();
            if (length != 0) {
                for (int i = 0; i < length; i++) {
                    if (!Character.isWhitespace(str.charAt(i))) {
                        return false;
                    }
                }
                return true;
            }
        }
        return true;
    }

    private static void b(String str, String str2) {
        Throwable th;
        FileWriter fileWriter = null;
        FileWriter fileWriter2;
        try {
            fileWriter2 = new FileWriter(new File(str), false);
            try {
                fileWriter2.write(str2);
                try {
                    fileWriter2.close();
                } catch (IOException e) {
                }
            } catch (Exception e2) {
                if (fileWriter2 != null) {
                    try {
                        fileWriter2.close();
                    } catch (IOException e3) {
                    }
                }
            } catch (Throwable th2) {
                Throwable th3 = th2;
                fileWriter = fileWriter2;
                th = th3;
                if (fileWriter != null) {
                    try {
                        fileWriter.close();
                    } catch (IOException e4) {
                    }
                }
                throw th;
            }
        } catch (Exception e5) {
            fileWriter2 = null;
            if (fileWriter2 != null) {
                fileWriter2.close();
            }
        } catch (Throwable th4) {
            th = th4;
            if (fileWriter != null) {
                fileWriter.close();
            }
            throw th;
        }
    }

    private static boolean b(String str) {
        boolean z = false;
        File file = new File(str);
        if (!file.exists()) {
            try {
                z = file.createNewFile();
            } catch (Exception e) {
            }
        }
        return z;
    }

    private static long c(String str, String str2) {
        File file = new File(str, str2);
        return file.exists() ? file.length() : 0;
    }

    private static boolean c(String str) {
        File file = new File(str);
        return !file.exists() ? file.mkdirs() : false;
    }
}
