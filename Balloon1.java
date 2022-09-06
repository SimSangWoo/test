import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Balloon1 {
	
	// ǳ���� �ִ� ������ ���߰�, �ּ� ������ ���߰� ����.
	// ������ x�� ����. (y > 0)
	static PriorityQueue<Double[]> angles = new PriorityQueue<>(new Comparator<Double[]>() {
		@Override
		public int compare(Double[] o1, Double[] o2) {
			if(o1[1] - o2[1] != 0)
				return (int)(o1[1] - o2[1]);
			else
				return (int)(o1[0] - o2[0]);
		}
	});
	static double[] lz = new double[2];
	static int cnt;
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("input.txt"));
		BufferedWriter bw = new BufferedWriter(new FileWriter("output.txt"));
		StringTokenizer st = new StringTokenizer(br.readLine(), " ");
		
		// ������ �߻� ��ǥ : (lz[0], lz[1])
		lz[0] = Double.parseDouble(st.nextToken());
		lz[1] = Double.parseDouble(st.nextToken());

		int i = 0;
		String input;

		while((input = br.readLine()) != null) {
			st = new StringTokenizer(input, " ");

			// ����� ���Ǹ� ���� ������ �߻� ��ǥ�� �������� ǳ���� ��ǥ ����.
			double x = Double.parseDouble(st.nextToken()) - lz[0];
			double y = Double.parseDouble(st.nextToken()) - lz[1];
			double r = Double.parseDouble(st.nextToken());
			double dSquare = x * x + y * y; 
			
			// ǳ���� �ִ� ������ ���߰� - �ּ� ������ ���߰�.
			double theta = Math.asin(2 * r * Math.sqrt(dSquare - r * r) / dSquare);
			// ǳ�� ���� ����.
			double centralAngle = Math.asin(y / Math.sqrt(dSquare));
			
			if(x == 0) {
				if(y > 0)
					centralAngle = Math.PI / 2;
				if(y < 0)
					centralAngle = 3 * Math.PI / 2;
			}
			if(y == 0) {
				if(x > 0)
					centralAngle = 0;
				if(x < 0)
					centralAngle = Math.PI;
			}
			if(x < 0)
				centralAngle = Math.PI - centralAngle;

			if(x > 0)
				if(y < 0)
					centralAngle = 2 * Math.PI + centralAngle;

			double maxAngle = (centralAngle + theta / 2) * 180 / Math.PI;
			double minAngle = (centralAngle - theta / 2) * 180 / Math.PI;
			
			if(maxAngle < minAngle)
				minAngle = 180 - minAngle;

			angles.add(new Double[] {maxAngle, minAngle});
			i++;
		}

		br.close();
		
		// ������ ������ ǳ���� �ּ� ���߰����� ������ ���� ������Ŵ.
		// �׷��ٰ� ������ ǳ���� ���� �پ��� ������ ������ ǳ������ ��� ��Ʈ��. (Priority Queue���� ����)
		// Priority Queue�� �� ������ �ݺ�.
		cnt = i;
		while(angles.size() > 1) {
			Double[] start = angles.poll();
			Double[] nextStart = angles.peek();

			// ǳ���� ��ġ�� ���� -> ������ �� �� ���� ǳ�� �ϳ��� ��Ʈ�� �� ����.
			if(nextStart[1] > start[0])
				continue;

			double limit = Math.min(start[0], nextStart[0]);
			angles.poll();
			cnt--;
			
			while(angles.size() > 1) {
				//ǳ���� ��ġ�� ���� -> ������ ǳ���� ���� �پ��� ����.
				if(limit < angles.peek()[1])
					break;
				cnt--;
				limit = Math.min(limit, angles.poll()[0]);
			}
		}

		bw.write(String.valueOf(cnt));
		bw.close();	
	}
}
