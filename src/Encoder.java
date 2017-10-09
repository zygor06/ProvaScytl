import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Encoder {

    private final int END = 0;
    private final int START_PKT = 1;
    private final int END_PKT = 2;
    private boolean showLog;

    private Map<String, String> table1 = new HashMap<>();
    public Map<String, String> table1keys = new HashMap<>();
    private Map<String, Integer> table2 = new HashMap<>();

    public Encoder(){
        inicializarTable1();
        inicializarTable1keys();
        showLog = false;
    }

    public void showLog(){
        this.showLog = true;
    }

    public void hideLog(){
        this.showLog = false;
    }

    private void inicializarTable1(){
        this.table1.put("0000", "11110");
        this.table1.put("0001", "01001");
        this.table1.put("0010", "10100");
        this.table1.put("0011", "10101");
        this.table1.put("0100", "01010");
        this.table1.put("0101", "01011");
        this.table1.put("0110", "01110");
        this.table1.put("0111", "01111");
        this.table1.put("1000", "10010");
        this.table1.put("1001", "10011");
        this.table1.put("1010", "10110");
        this.table1.put("1011", "10111");
        this.table1.put("1100", "11010");
        this.table1.put("1101", "11011");
        this.table1.put("1110", "11100");
        this.table1.put("1111", "11101");
    }

    private void inicializarTable1keys(){
        this.table1keys.put("11110","0000");
        this.table1keys.put("01001","0001");
        this.table1keys.put("10100","0010");
        this.table1keys.put("10101","0011");
        this.table1keys.put("01010","0100");
        this.table1keys.put("01011","0101");
        this.table1keys.put("01110","0110");
        this.table1keys.put("01111","0111");
        this.table1keys.put("10010","1000");
        this.table1keys.put("10011","1001");
        this.table1keys.put("10110","1010");
        this.table1keys.put("10111","1011");
        this.table1keys.put("11010","1100");
        this.table1keys.put("11011","1101");
        this.table1keys.put("11100","1110");
        this.table1keys.put("11101","1111");
    }

    public String decode(String input){

        ArrayList<String> decode_step1 = divideBytesIntoHexPackets(input);
        ArrayList<String> decode_step2a = hexListToBinList(decode_step1);
        ArrayList<String> decode_step2b = binListDecoderTable1(decode_step2a);
        ArrayList<String> decode_step2c = binListToHexList(decode_step2b);
        StringBuilder decode_step3 = new StringBuilder();
        for(String s : decode_step2c){
            decode_step3.append(s);
            decode_step3.append(" ");
        }

        String decode_step4 = hexToAscii(decode_step3.toString());

        //Invert message
        String decode_step5 = invertAscii(decode_step4);

        //Encode inverted message
        StringBuilder encode_step1 = new StringBuilder(input);
        String encode_step2 = asciiToHex(encode_step1.toString());
        ArrayList<String> encode_step3a = invertHexList(decode_step2c);
        ArrayList<String> encode_step3b = hexListToBinList(encode_step3a);
        ArrayList<String> encode_step3c = binListEncodeTable1(encode_step3b);
        ArrayList<String> encode_step3d = binListToHexList(encode_step3c);
        ArrayList<String> encode_step4 = listAddCode(encode_step3d);
        String encode_step5 = listToString(encode_step4);
        String encode_step6 = hexToAscii(encode_step5);

        return encode_step6;
    }

    @NotNull
    public String binToHex(String input){
        StringBuilder output = new StringBuilder();                         //StringBuilder output
        String[] arBinConverted =                                           //An array that is grouped by
                input.toString().split("(?<=\\G........)");           // each 8 chars of the input
        ArrayList<String> binConverted =                                    //ArrayList made from the previous
                new ArrayList<>(Arrays.asList(arBinConverted));             //array

        for(String s : binConverted) {
            int n = Integer.parseInt(s, 2);
            String hexString = Integer.toHexString(n);                      //convert the int to a hexadecimal string
            output.append("0x");                                            //and append de '0x' prefix
            output.append(hexString.toUpperCase());                         //make sure that the chars are uppercase
            output.append(" ");                                                                            //convert the binary string into a int
                                                       //add the space separator
        }

        return output.toString().trim();                                    //return the string without the last space
    }

    @NotNull
    public String hexToBin(String input, Boolean addSpace){
        StringBuilder output = new StringBuilder();                         //StringBuilder output
        ArrayList<String> hex =                                             //List with the input splited by space chars
                new ArrayList<>(Arrays.asList(input.split(" ")));     //
        ArrayList<String> bin = new ArrayList<>();                          //List with the binary string output
        if(addSpace){                                                       //if the paramatter addspace is true
            while(hex.size() % 4 != 0){                                     //add the '0x20' value while the input
                hex.add("0x20");                                            //divisible by four
            }
        }

        for(String s : hex){
            if(s.length() == 4){
                StringBuilder sbBin = new StringBuilder();
                int n = Integer.parseInt(s.substring(2,4), 16);
                String strBin = Integer.toBinaryString(n);
                for(int j = strBin.length(); j < 8; j++){
                    sbBin.append("0");
                }
                sbBin.append(strBin);
                bin.add(sbBin.toString());
            }

        }

        for(String s : bin) {                                               //for each string in the list
            if(!(s == null || s.isEmpty())){
                output.append(s);
            }
        }

        return output.toString();                                           //then return the output
    }

    @NotNull
    public String binEncodeTable(String input){
        StringBuilder output = new StringBuilder();                         //StringBuilder output
        ArrayList<String> bin =                                             //List with the binary strings
                new ArrayList<>(Arrays.asList(                              //created from the input splited from
                        input.split("(?<=\\G....)")));                //each four characters

        for(String s : bin){                                                //for each string in bin
            output.append(table1.get(s));                                   //append the string from the table1
        }

        return output.toString();                                           //then return the full string
    }

    @NotNull
    public String binDecodeTable(String input){
        StringBuilder output = new StringBuilder();                        //StringBuilder output
        ArrayList<String> bin =                                             //List with the binary strings
                new ArrayList<>(Arrays.asList(                              //created from the input splited by
                        input.split("(?<=\\G.....)")));               //each five characters

        for(String s : bin){
            if(!(s.isEmpty() || s.length() < 1)){
                output.append(table1keys.get(s));                        //append the string from the table1 decoded                 //append
            }
        }

        return output.toString();                                           //then return the full string
    }

    @NotNull
    public ArrayList<String> divideBytesIntoHexPackets(String input){
        ArrayList<String> packets =
                new ArrayList<>(Arrays.asList(
                        input.split("0xC6")));
        ArrayList<String> output = new ArrayList<>();
        for(String s : packets){
            String str = s.replaceAll
                    ("(0x6B|0x21)", "").trim();
            if(!(s.isEmpty() || s.length() < 1)){
                output.add(str.trim());
            }
        }

        return output;
    }

    @NotNull
    public String hexToAscii(String input){

        StringBuilder output = new StringBuilder();
        String[] arStr = input.split(" ");
        for(String s : arStr){
            int n = Integer.parseInt(s.substring(2,4), 16);
            char c = (char) n;
            output.append(c);
        }

        return output.toString().trim();
    }

    @NotNull
    public String invertAscii(String input){
        StringBuilder output = new StringBuilder();
        for(int i = input.length() - 1; i >= 0; i--){
            output.append(input.charAt(i));
        }

        return output.toString();
    }

    @NotNull
    public String asciiToHex(String input){
        StringBuilder in = new StringBuilder(input);
        StringBuilder output = new StringBuilder();
        for(int i = 0; i < input.length(); i++){
            int n = (int) in.charAt(i);
            String hex = Integer.toHexString(n).toUpperCase();
            output.append("0x");
            output.append(hex);
            output.append(" ");
        }

        return output.toString().trim();
    }

    @NotNull String invertBinList(ArrayList<String> input){
        StringBuilder output = new StringBuilder();

        for(int k = 0; k < input.size(); k++){
            String current = input.get(k);
            if(!current.isEmpty() || !current.equals("")){
                output.append("11000110");
                String[] arStr = current.split(" ");
                for(int i = arStr.length - 1; i >= 0; i--){
                    output.append(arStr[i]);
                }
                if(k == input.size() - 1){
                    output.append("00100001");
                }else{
                    output.append("01101011");
                }
            }
        }

        String result = output.toString();
        String remove = result.substring(8).trim();
        String nova = remove.substring(0,remove.length() - 8);

        return nova;
    }

    public ArrayList<String> hexListToBinList(ArrayList<String> input){

        ArrayList<String> output = new ArrayList<>();
        for(String s : input){
            output.add(hexToBin(s, false));
        }

        return output;
    }

    public void log(String message){
        if(showLog){
            System.out.println(message);
        }
    }

    public ArrayList<String> binListDecoderTable1(ArrayList<String> input) {
        ArrayList<String> output = new ArrayList<>();

        for(String s : input){
            output.add(binDecodeTable(s));
        }

        return output;
    }

    public ArrayList<String> binListToHexList(ArrayList<String> input) {
        ArrayList<String> output = new ArrayList<>();

        for(String s : input){
            output.add(binToHex(s.trim()));
        }

        return output;
    }

    public ArrayList<String> invertHexList(ArrayList<String> input) {
        ArrayList<String> output = new ArrayList<>();
        StringBuilder all = new StringBuilder();
        ArrayList<String> reverse = new ArrayList<>();

        for(String s : input){
            all.append(s);
            all.append(" ");
        }

        String[] arStr = all.toString().trim().split(" ");
        int limit = input.get(0).split(" ").length;
        for(int i = arStr.length - 1; i >= 0; i--){
            reverse.add(arStr[i]);
        }

        int current = 1;

        for(int i = input.size() - 1; i >= 0; i--){
            ArrayList<String> out = new ArrayList<>();
            StringBuilder finalStr = new StringBuilder();

            for(int j = 0; j < limit;j++){
                String atual;
                try{
                    atual = reverse.get(current);
                }catch (Exception e){
                    atual = "0x20";
                }

                out.add(atual);
                current++;
            }

            while(out.size() != limit){
                out.add("0x20");
            }

            for(String s : out){
                finalStr.append(s);
                finalStr.append(" ");
            }
            output.add(finalStr.toString().trim());
        }

        return output;
    }

    public ArrayList<String> binListEncodeTable1(ArrayList<String> input) {
        ArrayList<String> output = new ArrayList<>();

        for(String s : input){
            output.add(binEncodeTable(s));
        }

        return output;
    }

    public ArrayList<String> listAddCode(ArrayList<String> input) {
        ArrayList<String> output = new ArrayList<>();

        for(int i = 0; i < input.size(); i++){
            StringBuilder sb = new StringBuilder();
            sb.append("0xC6 ");
            sb.append(input.get(i));
            if(i == input.size() - 1){
                sb.append(" 0x21");
            }else{
                sb.append(" 0x6B");
            }

            output.add(sb.toString().trim());
        }

        return output;
    }

    public String listToString(ArrayList<String> input) {
        StringBuilder sb = new StringBuilder();
        for(String s : input){
            sb.append(s);
            sb.append(" ");
        }

        return sb.toString().trim();
    }
}
