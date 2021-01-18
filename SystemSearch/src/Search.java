import java.io.*;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Search {
    public static void main(String[] args) throws IOException, InterruptedException {
        //Код для нахождения пакетов на сайте https://security-tracker.debian.org/tracker/data/json

        String url = "https://security-tracker.debian.org/tracker/data/json";
        Document doc = Jsoup.connect(url).ignoreContentType(true).userAgent("Mozilla").get();
        ArrayList<String> Version = new ArrayList<String>();
        ArrayList<String> OnlyName = new ArrayList<String>();
        ArrayList<String> Version_new = new ArrayList<String>();
        ArrayList<String> Version_last = new ArrayList<String>();
        ArrayList<String> OnlyName_new = new ArrayList<String>();
        ArrayList<String> Result_list = new ArrayList<String>();
        String Name_text = doc.body().text();
        String Version_text = doc.body().text();
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
            writer1.write(name+"\n");
            //System.out.println(OnlyName_new.get(j));
        }
        Integer count = 0;
        for (int i = 0; i < Len; i++) {
            for (int j = 0; j < Len_vers; j++) {
                if (OnlyName.get(i).equals(OnlyName_new.get(j))){
                    Long vers1 = Long.valueOf(Version.get(i));
                    Long vers2 = Long.valueOf(Version_new.get(j));
                    System.out.println(Version.get(i) + "\t" + Version_new.get(j));
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


