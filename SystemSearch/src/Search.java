import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Search {
    public static void main(String[] args) throws IOException, InterruptedException {
        String s;
        ArrayList<String> Names = new ArrayList<String>();
        Process p;
        try {
            p = Runtime.getRuntime().exec("dpkg --get-selections");
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null)
            Names.add(s);
            p.waitFor();
            p.destroy();
        }
        catch (Exception e) {

        }
        Names = DeleteInstallWord(Names);


    }
    public static ArrayList<String> DeleteInstallWord(ArrayList<String> Names)
    {
        int Len = Names.size();
        ArrayList<String> Result = new ArrayList<String>();
        String s;
        for(int i = 0; i < Len; i++)
        {
            s = Names.get(i);
            s = s.substring(0,s.indexOf("\t"));
//            s.replace(" ","");
            System.out.println(s);
            Result.add(s);
        }
        return Result;
    }

}
