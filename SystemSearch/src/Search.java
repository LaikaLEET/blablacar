import java.io.*;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.util.Scanner;



public class Search {
    public static void main(String[] args) throws IOException, InterruptedException {
        ArrayList<String> Version = new ArrayList<String>();
        ArrayList<String> OnlyName = new ArrayList<String>();
        ArrayList<String> Version_new = new ArrayList<String>();
        ArrayList<String> Version_last = new ArrayList<String>();
        ArrayList<String> OnlyName_new = new ArrayList<String>();
        ArrayList<String> Result_list = new ArrayList<String>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader("names.txt"));
        int i1 = 0;
        ArrayList<String> In = new ArrayList<String>();
        String S = "";
        int symbol = bufferedReader.read();
        while (symbol != -1) {  // Когда дойдём до конца файла, получим '-1'
            // Что-то делаем с прочитанным символом
            // Преобразовать в char:
            symbol = bufferedReader.read(); // Читаем символ
            char c = (char) symbol;
            if (c != '\n') {
                S = S + c;
            } else {
                In.add(S);
                S = "";
                i1++;
            }
        }

        for (int i = 0 ; i < In.size() ; i++) {
            String L = In.get(i);
            String[] str_array = In.get(i).split("/");
            OnlyName_new.add(str_array[0]);
            Version_new.add(str_array[1]);

        //Код Леши для выполнения команд в Linux
        String s;
        ArrayList<String> Names = new ArrayList<String>();
        Process p;
        try {
            p = Runtime.getRuntime().exec("dpkg-query -W");
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null)
                Names.add(s);
            p.waitFor();
            p.destroy();
        } catch (Exception e) {

        }
        int Len = Names.size();
        int Len_vers = Version_new.size();
        for (int i = 0; i < Len; i++) {
            s = Names.get(i);
            String version = s.substring(s.indexOf("\t"));
            Version.add(version);
            Version.set(i, version.replace("\t", ""));
            Version.set(i, Version.get(i).replaceAll("[^\\d.]", ""));
            Version.set(i, Version.get(i).replaceAll("\\.", ""));
            String onlyname = s.substring(0, s.indexOf("\t"));
            OnlyName.add(onlyname);
        }

        for (int j = 0; j < Len_vers; j++) {
            String version_new = Version_new.get(j);
            String name = OnlyName_new.get(j);
            Version_last.add(Version_new.get(j));
            Version_new.set(j, Version_new.get(j).replaceAll("[^\\d.]", ""));
            Version_new.set(j, Version_new.get(j).replaceAll("\\.", ""));
            OnlyName_new.set(j,name.replace("\"", ""));
        }
        Integer count = 0;
        for (int i = 0; i < Len; i++) {
            for (int j = 0; j < Len_vers; j++) {
                if (OnlyName.get(i).equals(OnlyName_new.get(j))){
                    Long vers1 = Long.valueOf(Version.get(i));
                    Long vers2 = Long.valueOf(Version_new.get(j));
                    if(vers1 < vers2){
                        count ++;
                        Result_list.add("Package:"+OnlyName_new.get(j) + " Version:" + Version_last.get(j));
                    }

                }
            }
            }


        System.out.println("Пакеты и версии, которые нужно установить для устранения неуязвимостей");
        Integer Len_result = Result_list.size();
        for (int i = 0; i < Len_result; i++) {
            System.out.println(i+1 + ") " + Result_list.get(i));
        }


        //проверка на ошибки

    }
}
