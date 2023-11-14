import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Utils {
    public static List<Integer> Range(int start, int end) {
        return IntStream.iterate(start, i -> i+1)
            .limit(end)
            .boxed()
            .collect(Collectors.toList());
    }

    public static void PrintResultSet(ResultSet input) {
        try {
            ResultSetMetaData metaData = input.getMetaData();
            int columnNumber = metaData.getColumnCount();
            int[] lengths = new int[columnNumber];
            input.first();
            do {
                for (int i : Utils.Range(1, columnNumber)) {
                    String out = input.getString(i);
                    if (out.length() > lengths[i-1]) lengths[i-1] = out.length();
                }
            } while (input.next());
            input.first();
            do {
                System.out.print(" |");
                for (int i : Utils.Range(1, columnNumber)) {
                    String out = input.getString(i);
                    String formatted = String.format(" %%%ss |", lengths[i-1]);

                    System.out.print(String.format(formatted, out));
                }
                System.out.println();
            } while(input.next());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
