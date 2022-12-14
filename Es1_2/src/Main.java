/* -------------------------------------------------------------------------
 * This program simulates a single-server FIFO service node using arrival
 * times and service times read from a text file.  The server is assumed
 * to be idle when the first job arrives.  All jobs are processed completely
 * so that the server is again idle at the end of the simulation.   The
 * output statistics are the average interarrival time, the average service
 * time, the average delay in the queue, and the average wait in the service
 * node.
 *
 * Name              : Ssq1.java  (Single Server Queue, version 1)
 * Authors           : Steve Park & Dave Geyer
 * Translated by     : Jun Wang & Richard Dutton
 * Language          : Java
 * Latest Revision   : 6-16-06
 * Program ssq1      : Section 1.2
 * -------------------------------------------------------------------------
 */

import java.io.*;
import java.text.DecimalFormat;
import java.util.StringTokenizer;

class Ssq2Sum {                                 /* sum of ...           */
    double delay;                                 /*   delay times        */
    double wait;                                  /*   wait times         */
    double service;                               /*   service times      */
    double interarrival;                          /*   interarrival times */





    void initSumParas() {
        delay = 0.0;
        wait = 0.0;
        service = 0.0;
        interarrival = 0.0;


    }
}

class Ssq2 {
    static String FILENAME = "/Users/andreasvevamonteamaro/Downloads/ssq2.dat";          /* input data file */
    static double START = 0.0;

    public static void main(String[] args) throws IOException {

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(FILENAME);
        } catch (FileNotFoundException fnfe) {
            System.out.println("Cannot open input file" + FILENAME);
            System.exit(1);
        }

        InputStreamReader r = new InputStreamReader(fis);
        BufferedReader in = new BufferedReader(r);
        try {
            String line = null;
            StringTokenizer st = null;
            long   index     = 0;                     /* job index            */
            //double arrival   = START;                 /* arrival time         */
            double arrival=0.0;
            double intarrival =START;
            double oldarrival=0.0;
            double delay;                             /* delay in queue       */
            double service;                           /* service time         */
            double wait;                              /* delay + service      */
            double departure = START;                 /* departure time       */

            double throughput;
            double input_rate;
            double service_rate;
            double traffic_intensity;
            double server_utilization;
            double avg_n_in_service;
            double total_completation_time;

            double lastservice=0.0;

            int numOfDelay=0;                         /* number of delay job  */
            double maxDelay=0;                        /* max delay time       */
            double time=400.0;                        /* time                 */
            int jobInServiceFacility=0;                    /*number of jobs in the service node at a specified time */

            Ssq2Sum sum = new Ssq2Sum();
            sum.initSumParas();

            while ( (line = in.readLine()) != null ) {
                index++;

                st = new StringTokenizer(line);

                intarrival = Double.parseDouble(st.nextToken());

                arrival= intarrival + oldarrival; /* Ti= ai-a(i-1) so ai= Ti + a(i-1) */

                if (arrival < departure)
                    delay    = departure - arrival;       /* delay in queue    */
                else
                    delay    = 0.0;                       /* no delay          */

                if(maxDelay<delay)
                    maxDelay=delay;
                if(delay!=0.0)
                    numOfDelay++;

                service = Double.parseDouble(st.nextToken());
                wait         = delay + service;
                departure    = arrival + wait;          /* time of departure */
                sum.delay   += delay;
                sum.wait    += wait;
                sum.service += service;
                lastservice = service;

                if(arrival<time & time<departure)
                    jobInServiceFacility++;

            oldarrival=arrival;     /* a(i-1)*/
            }
            sum.interarrival = arrival - START;


            input_rate = 1 / (sum.interarrival/index);
            service_rate= 1 / (sum.service/index);
            traffic_intensity = input_rate/ service_rate;
            total_completation_time = arrival+sum.delay+lastservice;
            throughput= index/ total_completation_time ;
            server_utilization = sum.service/ total_completation_time;
            avg_n_in_service = sum.service/total_completation_time;

            DecimalFormat f = new DecimalFormat("###0.00");
            System.out.println(" Modified version");
            System.out.println("\nfor " + index + " jobs");
            System.out.println("   average interarrival time =  " + f.format(sum.interarrival / index));
            System.out.println("   average service time .... =  " + f.format(sum.service / index));
            System.out.println("   average delay ........... =  " + f.format(sum.delay / index));
            System.out.println("   average wait ............ =  " + f.format(sum.wait / index));


            System.out.println("   input rate ...............=  " + f.format(input_rate));
            System.out.println("   service rate ............ =  " + f.format(service_rate));
            System.out.println("   traffic intensity ....... =  " + f.format(traffic_intensity));
            System.out.println("   throughput .............. =  " + f.format(throughput));
            System.out.println("   server utilization ...... =  " + f.format(server_utilization));
            System.out.println("   average number in service =  " + f.format(avg_n_in_service));
            System.out.println("   max delay................ =  " + f.format(maxDelay));
            System.out.print("   the number of jobs in the service facility at time " +time );
            System.out.println(" is ="+  jobInServiceFacility);
            System.out.println("   number of delay ............ =  " + (numOfDelay));
            System.out.println("   the proportion of customer that were delayed ="+ f.format((double) numOfDelay/index));


        } catch (EOFException eofe) {
            System.out.println("Ssq2:" + eofe);
        }
        // if the file opened okay, make sure we close it
        fis.close();
    }

}