/*
 * CS61C Spring 2014 Project2
 * Reminders:
 *
 * DO NOT SHARE CODE IN ANY WAY SHAPE OR FORM, NEITHER IN PUBLIC REPOS OR FOR DEBUGGING.
 *
 * This is one of the two files that you should be modifying and submitting for this project.
 */
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.Math;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class PossibleMoves {
    public static class Map extends Mapper<IntWritable, MovesWritable, IntWritable, IntWritable> {
        int boardWidth;
        int boardHeight;
        boolean OTurn;
        /**
         * Configuration and setup that occurs before map gets called for the first time.
         *
         **/
        @Override
        public void setup(Context context) {
            // load up the config vars specified in Proj2.java#main()
            boardWidth = context.getConfiguration().getInt("boardWidth", 0);
            boardHeight = context.getConfiguration().getInt("boardHeight", 0);
            OTurn = context.getConfiguration().getBoolean("OTurn", true);
        }

        /**
         * The map function for the first mapreduce that you should be filling out.
         */
        @Override
        public void map(IntWritable key, MovesWritable val, Context context) throws IOException, InterruptedException {
            String str = Proj2Util.gameUnhasher(key.get(), boardWidth, boardHeight);
            char turn;
            if (val.getStatus() != 0) {
                return;
            }
            if (OTurn) {
                turn = 'O';
            } else {
                turn = 'X';
            }
            for (int i = 0; i < boardWidth; i++) {
                char[] stra = str.toCharArray();
                if (stra[boardHeight * i] == ' ') {
                    stra[boardHeight * i] = turn;
                    String s = new String(stra);
                    context.write(new IntWritable(Proj2Util.gameHasher(s, boardWidth, boardHeight)), key);
                } else {
                    int k = 0;
                    while (k < boardHeight) {
                        if (stra[boardHeight * i + k] == ' ') {
                            stra[boardHeight * i + k] = turn;
                            String s = new String(stra);
                            context.write(new IntWritable(Proj2Util.gameHasher(s, boardWidth, boardHeight)), key);
                            k = boardHeight;
                        }
                        k++;
                    }
                }
            }
		//moves.add(s)
		// }
		//int[] newmoves = new int[moves.size()];
		//for (int a = 0; a < moves.size(); a++) {
		//	newmoves[a] = Proj2Util.gameHasher(moves.get(a), boardWidth, boardHeight);
		//  }
		// val.setMoves(newmoves);
		//context.write(key, val);
        }
    }

    public static class Reduce extends Reducer<IntWritable, IntWritable, IntWritable, MovesWritable> {

        int boardWidth;
        int boardHeight;
        int connectWin;
        boolean OTurn;
        boolean lastRound;
        /**
         * Configuration and setup that occurs before reduce gets called for the first time.
         *
         **/
        @Override
        public void setup(Context context) {
            // load up the config vars specified in Proj2.java#main()
            boardWidth = context.getConfiguration().getInt("boardWidth", 0);
            boardHeight = context.getConfiguration().getInt("boardHeight", 0);
            connectWin = context.getConfiguration().getInt("connectWin", 0);
            OTurn = context.getConfiguration().getBoolean("OTurn", true);
            lastRound = context.getConfiguration().getBoolean("lastRound", true);
        }

        /**
         * The reduce function for the first mapreduce that you should be filling out.
         */
        @Override
        public void reduce(IntWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            ArrayList<Integer> moves = new ArrayList<Integer>();
            String str = Proj2Util.gameUnhasher(key.get(), boardWidth, boardHeight);
            byte win;
            MovesWritable mw;
            if (OTurn) {
                win = 1;
            } else {
                win = 2;
            }
            for (IntWritable elem : values) {
                moves.add(elem.get());
            }
            int[] newmoves = convertIntegers(moves);
            if (Proj2Util.gameFinished(str, boardWidth, boardHeight, connectWin)) {
                mw = new MovesWritable(win, 0, newmoves);
            } else if (lastRound) {
                win = 3;
                mw = new MovesWritable(win, 0, newmoves);
            } else {
                win = 0;
                mw = new MovesWritable(win, newmoves);
            }
            context.write(key, mw);
        }

    public static int[] convertIntegers(ArrayList<Integer> integers) {
        int[] ret = new int[integers.size()];
        for (int i=0; i < ret.length; i++) {
            ret[i] = integers.get(i).intValue();
        }
        return ret;
    }
    }
}
