package com.cgit.ogdensuntamedwomen.model;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {
    private class StringDArray {
        private String[] data=new String[0];
        private int used=0;
        public void add(String str) {
            if (used >= data.length){
                int new_size= used+1;
                String[] new_data=new String[new_size];
                java.lang.System.arraycopy( data,0,new_data,0,used);
                data=new_data;
            }
            data[used++] = str;
        }
        public int length(){
            return  used;
        }
        public String[] get_araay(){
            return data;
        }
    }
    private  Context context;
    public CSVReader(Context context){
        this.context=context;
    }
    public ArrayList<Places> read(InputStream inputStream){
        ArrayList<Places> resultList = new ArrayList();
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String csvLine;
            final char Separator = ',';
            final char Delimiter = '"';
            final char LF = '\n';
            final char CR = '\r';
            boolean quote_open = false;
            reader.readLine();
            while ((csvLine = reader.readLine()) != null) {
                //String[] row = csvLine.split(",");// simple way
                StringDArray a=new StringDArray();
                String token="";
                csvLine+=Separator;
                for(char c:csvLine.toCharArray()){
                    switch (c){
                        case LF: case CR:// not required as we are already read line
                            quote_open=false;
                            a.add(token);
                            token="";
                            break;
                        case Delimiter:
                            quote_open=!quote_open;
                            break;
                        case Separator:
                            if(quote_open==false){
                                a.add(token);
                                token="";
                            }else{
                                token+=c;
                            }
                            break;
                        default:
                            token+=c;
                            break;
                    }
                }
                if(a.length()>0 ) {
                    if(resultList.size()>0){

                            String[] row = a.get_araay();
                            resultList.add(new Places(row[0],row[1],row[2],row[3],row[4],row[5],row[6],row[7]));

                    }else{
                        String[] row = a.get_araay();
                        resultList.add(new Places(row[0],row[1],row[2],row[3],row[4],row[5],row[6],row[7]));//header row
                    }
                }
            }
            inputStream.close();
        }catch (Exception e){
            Toast.makeText(context,"Error : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return resultList;
    }
    public ArrayList<PlaceContent> readPlacesContent(InputStream inputStream,String id){
        ArrayList<PlaceContent> resultList = new ArrayList();
        try{

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String csvLine;
            final char Separator = ',';
            final char Delimiter = '"';
            final char LF = '\n';
            final char CR = '\r';
            boolean quote_open = false;
            reader.readLine();
            while ((csvLine = reader.readLine()) != null) {
                //String[] row = csvLine.split(",");// simple way
                StringDArray a=new StringDArray();
                String token="";
                csvLine+=Separator;
                for(char c:csvLine.toCharArray()){
                    switch (c){
                        case LF: case CR:// not required as we are already read line
                            quote_open=false;
                            a.add(token);
                            token="";
                            break;
                        case Delimiter:
                            quote_open=!quote_open;
                            break;
                        case Separator:
                            if(quote_open==false){
                                a.add(token);
                                token="";
                            }else{
                                token+=c;
                            }
                            break;
                        default:
                            token+=c;
                            break;
                    }
                }

                        String[] row = a.get_araay();
                        if (id.equals(row[0])){
                            Log.i("check run","running");
                                resultList.add(new PlaceContent(row[0],row[1],row[2],row[3]));
                            }
                    }

            inputStream.close();
        }catch (Exception e){
            Toast.makeText(context,"Error : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return resultList;
    }
}