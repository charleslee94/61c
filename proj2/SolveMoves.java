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

public class SolveMoves {
    public static class Map extends Mapper<IntWritable, MovesWritable, IntWritable, ByteWritable> {
        /**
         * Configuration and setup that occurs before map gets called for the first time.
         *
         **/
        @Override
        public void setup(Context context) {
        }

        /**
         * The map function for the second mapreduce that you should be filling out.
         */
        @Override
        public void map(IntWritable key, MovesWritable val, Context context) throws IOException, InterruptedException {
            /**byte value = val.getValue();
            value += 4;
            ByteWritable byte1 = new ByteWritable(value); */
            int[] moves = val.getMoves();
            for (int parent : moves) {
                context.write(new IntWritable(parent), new ByteWritable(val.getValue()));
            }
        }
    }

    public static class Reduce extends Reducer<IntWritable, ByteWritable, IntWritable, MovesWritable> {

        int boardWidth;
        int boardHeight;
        int connectWin;
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
            connectWin = context.getConfiguration().getInt("connectWin", 0);
            OTurn = context.getConfiguration().getBoolean("OTurn", true);
        }

        /**
         * The reduce function for the first mapreduce that you should be filling out.
         */
        @Override
        public void reduce(IntWritable key, Iterable<ByteWritable> values, Context context) throws IOException, InterruptedException {
            ArrayList<Byte> vals = new ArrayList<Byte>();
            for (ByteWritable val : values) {
                vals.add(val.get());
            }
            if ( vals.get(0) > 3) {
                return;
            }
            Collections.sort(vals);
            Byte bestVal = 0;
            int owin;
            for (Byte val : vals) {
                if (OTurn) {
                    owin = 1;
                } else {
                    owin = 2;
                }
                if (val > 0) {
                    if ((val & 1) == owin) {
                        bestVal = val;
                        break;
                    }
                } else {
                    if ((val & 1) == 0) {
                        bestVal = val;
                        break;
                    }
                }
            }
            if (bestVal == 0) {
                bestVal = vals.get(vals.size() -1);
            }
            int[] parInts = generate(key);
            MovesWritable fin = new MovesWritable(bestVal, parInts);
            int mte = fin.getMovesToEnd();
            mte++;
            fin.setMovesToEnd(mte);
            context.write(key, fin);

        }
 
        private int[] generate(IntWritable child) {
            String board = Proj2Util.gameUnhasher(child.get(), boardWidth, boardHeight);
            char[] boardArray = board.toCharArray();
            int[] parents = new int[boardWidth];
            int c = 0;
            for (int i = 0; i < boardWidth; i++) {
                for (int j = boardHeight - 1; j >= 0 ; j--) {
                    if (!OTurn){
                        if (boardArray[i * boardHeight + j] == 'O') {
                            StringBuilder str = new StringBuilder(board);
                            str.setCharAt(i * boardHeight + j, ' ');
                            parents[c] = Proj2Util.gameHasher(str.toString(), boardWidth, boardHeight);
                            c++;
                            break;
                        }
                        if (boardArray[i * boardHeight + j] == 'X') {
                            break;
                        }
                    } else {
                        if (boardArray[i * boardHeight + j] == 'O') {
                            break;
                        }
                        if (boardArray[i * boardHeight + j] == 'X') {
                            StringBuilder str = new StringBuilder(board);
                            str.setCharAt(i * boardHeight + j, ' ');
                            parents[c] = Proj2Util.gameHasher(str.toString(), boardWidth, boardHeight);
                            c++;
                            break;
                        }
                    }
                }
            }
            return parents;
        }
    }
}
