package src;
import java.io.IOException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.io.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.util.Random;


public class AgingPagingGraph {
    
    public static int agingPF(String fname, int n) {
        int totalPFaults = 0;
        int low = 0, high = 128, pagesN = 0;
        File file = new File(fname);
        int pFaults[][];
        int pages[] = new int[10000];
        int frames[][] = new int[n][2];
        
        BufferedReader reader = null;
        
        try {
            String text = null;
            reader = new BufferedReader(new FileReader(file));
            while ((text = reader.readLine()) != null) { pagesN++; }
            reader.close();
            
            pFaults = new int[pagesN][2];
            
            reader = new BufferedReader(new FileReader(file));
            
            while ((text = reader.readLine()) != null) {  

                String[] v = text.split(" ");
                pages[low] = (Integer.parseInt(v[1]));
                low++;
            }
            
            for(int p : pages) {
            
                boolean notFound = false, doBreak = false;
            
                while(!notFound) {
                
                    for(int f[] : frames) {
                    
                        if(f[0] == p) {
                        
                            f[1] = f[1] | high;
                            notFound = false;
                            doBreak = true;
                            break;
                        }
                    }
                
                    notFound = true;
                }
            
                if(notFound == true && !doBreak) {
            
                    for(int f[] : frames) {
                    
                        f[1] = f[1] >> 1;
                    }
                
                    int lowest = 0;
                
                    for(int j = 0; j < n-1; j++) {
                        
                        if(frames[j][1] > frames[j+1][1]) {
                        
                            lowest = j+1;
                        }
                    }
                    
                    if(p == 0) { break; }
                    
                    frames[lowest][0] = p;
                    frames[lowest][1] = 128;
                    pFaults[p-1][0] = p;
                    pFaults[p-1][1] = pFaults[p-1][1]+1;
                }
            }
        
            System.out.println("Final Frames" );
            for(int g[] : frames) {
            
                System.out.println(g[0] + " " + g[1]);
            }
            
            
            System.out.println("Page faults");
            for(int g[] : pFaults) {
            
                if(g[0] > 0) {
                
                    totalPFaults += g[1];
                    System.out.println(g[0] + " " + g[1]);
                    
                }
            }
            
            System.out.println("Total page faults = " + totalPFaults);
            
        }
        
        catch (FileNotFoundException e) {
                e.printStackTrace();
        } catch (IOException e) {
                e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                   reader.close();
                }
            } catch (IOException e) {
            }
        
        }
        return totalPFaults;
    }
    
    public static void main(String[] args) throws IOException {
        
        Scanner s = new Scanner(System.in);
        System.out.println("Please enter the maximum number of frames");
        int n = s.nextInt();
        s.close();
        s = new Scanner(System.in);
        System.out.println("Please enter the input file name");
        String fname = s.nextLine();
        double pageFaults[] = new double[n];
        double frames[] = new double[n];
        for(int i = 1; i <= n; i++) {
            frames[i-1] = i;
            pageFaults[i-1] = agingPF(fname, i);
        }
        
        
        Plot plot = Plot.plot(Plot.plotOpts().
				title("Page Faults Rate").
				legend(Plot.LegendFormat.RIGHT)).	
			xAxis("Page Frames", Plot.axisOpts()).
			yAxis("Page Faults", Plot.axisOpts());
	plot.series("Input 3" , Plot.data().xy(frames,pageFaults), null);

        plot.save("sample_minimal4", "png");
    
        
        
    }

}

    
   

