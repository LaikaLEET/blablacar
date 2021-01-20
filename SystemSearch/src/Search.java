import java.io.*;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.util.Scanner;



public class Search {
    public static void main(String[] args) throws IOException, InterruptedException {
        //Код для нахождения пакетов на сайте https://security-tracker.debian.org/tracker/data/json
        ArrayList<String> Version = new ArrayList<String>();
        ArrayList<String> OnlyName = new ArrayList<String>();
        ArrayList<String> Version_new = new ArrayList<String>();
        ArrayList<String> Version_last = new ArrayList<String>();
        ArrayList<String> OnlyName_new = new ArrayList<String>();
        ArrayList<String> Result_list = new ArrayList<String>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader("new.txt"));
        int i1 = 0;
        ArrayList<String> In = new ArrayList<String>();
        String S = "";
        int symbol = bufferedReader.read();
        while (symbol != -1) {  // Когда дойдём до конца файла, получим '-1'
            // Что-то делаем с прочитанным символом
            // Преобразовать в char:
            // char c = (char) symbol;
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
            //System.out.println(L);

//            String NM = In.get(i).substring(0, In.get(i).indexOf("/"));
//
//            String Ver = In.get(i).substring(In.get(i).indexOf("/"));
            String[] str_array = In.get(i).split("/");
            //System.out.println(str_array[0]);
            OnlyName_new.add(str_array[0]);
            Version_new.add(str_array[1]);
            System.out.println(OnlyName_new.get(i));
            System.out.println("\n");
            System.out.println(Version_new.get(i));
        }
//        Scanner scan = new Scanner(reader);
//        String url = "https://security-tracker.debian.org/tracker/data/json";
//        Document doc = Jsoup.connect(url).ignoreContentType(true).userAgent("Mozilla").get();
        String Name_text = " ";

//        while (scan.hasNextLine()){
//            Name_text += scan.nextLine();
////            System.out.println(scan.nextLine());
//        }
//        String Name_text = doc.body().text();
//        String test = Name_text;
        //Gson gson = new Gson();
        // test = gson.toJson(doc.body().text());
//        System.out.println(Name_text);
        //JsonElement ar = new JsonParser().parse();

        String Version_text = "";
        String result_name = "1";
        String result_vers = " ";
        Name_text = Name_text.substring(1);
        //Цикл для имен
        for (String retval : Name_text.split("}}}},")) {
            result_name = (retval.split(":")[0]);
            OnlyName_new.add(result_name);
            //System.out.println(OnlyName_new);
//            writer.write(result_name + "\n");
        }
        //Цикл для версий
        for (String retval : Version_text.split("}}}},")) {
            try {
                result_vers = (retval.split("\"fixed_version\":")[1]);
                result_vers = (result_vers.split(",\"urgency\":")[0]);
                Version_new.add(result_vers);
            }
            catch (RuntimeException e){
                 Version_new.add("-1");
            }
//            System.out.println(Version_new);
        }
        //Код для ручной сверки версий. Запись производится в файл
//        int Len1 = Version_new.size();
//        for (int i = 0; i < Len1; i++) {
//            writer.write(Version_new.get(i) + "\n");
//        }
        //Конец

//        FileReader reader = new FileReader("sources.txt");
//        int c;
//        while ((c = reader.read()) != -1) {
//            System.out.print((char) c);
//        }

        //Код для поиска fixed_version


        //Код Леши для выполнения команд в Linux
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
        int Len_vers = Version_new.size();
        //Добавление разбиения на имя и версию. Нашел базу данных, где подобное разбиение по-умолчанию. https://security-tracker.debian.org/tracker/DSA-3638-1 - пример уязвимого пакета
        FileWriter writer = new FileWriter("sources.txt", false);
        FileWriter writer1 = new FileWriter("sources1.txt", false);
        for (int i = 0; i < Len; i++) {
            s = Names.get(i);
            String version = s.substring(s.indexOf("\t"));
            Version.add(version);
            //version = version.replace("\t", "");
            Version.set(i, version.replace("\t", ""));
            Version.set(i, Version.get(i).replaceAll("[^\\d.]", ""));
            Version.set(i, Version.get(i).replaceAll("\\.", ""));
            String onlyname = s.substring(0, s.indexOf("\t"));
            OnlyName.add(onlyname);
            //System.out.println(s);
//            System.out.println(onlyname);
//            System.out.println(version);
//            System.out.println(OnlyName.get(i));
        }
//        System.out.println("\n");
//        System.out.println("\n");
//        System.out.println("\n");
        for (int j = 0; j < Len_vers; j++) {
            String version_new = Version_new.get(j);
            String name = OnlyName_new.get(j);
            //System.out.println(Len_vers);
            Version_new.set(j,version_new.replace("\"", ""));
            Version_last.add(Version_new.get(j));
            Version_new.set(j, Version_new.get(j).replaceAll("[^\\d.]", ""));
            Version_new.set(j, Version_new.get(j).replaceAll("\\.", ""));
            OnlyName_new.set(j,name.replace("\"", ""));
            writer1.write(Version_last.get(j)+"\n");
            //System.out.println(OnlyName_new.get(j));
        }
        Integer count = 0;
        for (int i = 0; i < Len; i++) {
            for (int j = 0; j < Len_vers; j++) {
                if (OnlyName.get(i).equals(OnlyName_new.get(j))){
                    Long vers1 = Long.valueOf(Version.get(i));
                    Long vers2 = Long.valueOf(Version_new.get(j));
                    //System.out.println(Version.get(i) + "\t" + Version_new.get(j));
                    if(vers1 < vers2){
                        count ++;
                        Result_list.add("Package:"+OnlyName_new.get(j) + " Version:" + Version_last.get(j));
                    }

                }
            }
            }

//        OnlyName_new.retainAll(OnlyName);
//
//        Version_new.retainAll(Version);
        System.out.println("Пакеты и версии, которые нужно установить для устранения неуязвимостей");
        Integer Len_result = Result_list.size();
        for (int i = 0; i < Len_result; i++) {
            System.out.println(i+1 + ") " + Result_list.get(i));
        }
        writer.close();
        writer1.close();

        //проверка на ошибки

    }
}
