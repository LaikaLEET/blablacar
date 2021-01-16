import java.io.*;
import java.util.ArrayList;

public class Search {
    public static void main(String[] args) throws IOException, InterruptedException {
        String s;
        ArrayList<String> Names = new ArrayList<String>();
        Process p;
        try {
//            p = Runtime.getRuntime().exec("dpkg --get-selections");
//            p = Runtime.getRuntime().exec("dpkg --get-selections");
            p = Runtime.getRuntime().exec("dpkg-query -W");
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null)
                Names.add(s);
            p.waitFor();
            p.destroy();
        } catch (Exception e) {

        }
//        Names = DeleteInstallWord(Names);
        int Len = Names.size();
        //Добавление разбиения на имя и версию. Нашел базу данных, где подобное разбиение по-умолчанию. https://security-tracker.debian.org/tracker/DSA-3638-1 - пример уязвимого пакета
        ArrayList<String> Version = new ArrayList<String>();
        ArrayList<String> OnlyName = new ArrayList<String>();
        for(int i = 0; i < Len; i++)
        {
            s = Names.get(i);
            String version = s.substring(s.indexOf("\t"));
            Version.add(version);
            version = version.replace("\t","");
            String onlyname = s.substring(0, s.indexOf("\t"));
            OnlyName.add(onlyname);
            System.out.println(s);
            System.out.println(onlyname);
            System.out.println(version);
        }
        //проверка на ошибки

    }





//    public static ArrayList<String> DeleteInstallWord(ArrayList<String> Names)
//    {
//        int Len = Names.size();
//        ArrayList<String> Result = new ArrayList<String>();
//        String s;
//        for(int i = 0; i < Len; i++)
//        {
//            s = Names.get(i);
//            s = s.substring(0,s.indexOf("\t"));
////            s.replace(" ","");
//            System.out.println(s);
//            Result.add(s);
//        }
//        return Result;
//    }

}