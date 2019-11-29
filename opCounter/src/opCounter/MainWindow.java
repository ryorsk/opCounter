package opCounter;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;


public class MainWindow {
	private OpenCsv ioCsv = new OpenCsv();
	private String fileName = "";
	DefaultTableModel tableModel;
	private JFrame frame;
	private final Action actCsvOpen = new csvOpen();
	private JTable table;
	private DefaultComboBoxModel<String> conboBoxModel;
	private final Action actGetSelectedColumn = new getSelectedColumn();
	private final Action actCalcOps = new calcOps();
	private JTextField txt_allQso;
	private JTextField txt_u20Qso;
	private JTextField txt_u19Qso;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		//swingのlook and feelを現在のOSにあわせて適応
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			JOptionPane.showMessageDialog(null, "Windowサイズの変更ができませんでした．このまま起動しますが，DPIが高い環境では字が小さくなります．");
			e.printStackTrace();
		}


		frame = new JFrame();
		frame.setTitle("オペレータカウンタ");
		frame.setBounds(100, 100, 270, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		//CSVオープンボタン
		JButton btnCsvopen = new JButton("csvOpen");
		btnCsvopen.setAction(actCsvOpen);
		btnCsvopen.setBounds(12, 36, 230, 30);
		frame.getContentPane().add(btnCsvopen);

		JLabel lbl_2 = new JLabel("2. オペレータ一覧の列を選ぶ");
		lbl_2.setBounds(12, 65, 230, 26);
		frame.getContentPane().add(lbl_2);

		//オペレータリスト選択（1行目を選択する）
		String[] csvFirstColumnList = {"(ファイルを開いてください)"};
		conboBoxModel = new DefaultComboBoxModel<String>(csvFirstColumnList);
		JComboBox<String> csvFirstColumn = new JComboBox<String>(conboBoxModel);
		//csvFirstColumn.setPreferredSize(new Dimension(30, 30));

		csvFirstColumn.setBounds(12, 90, 160, 30);
		frame.getContentPane().add(csvFirstColumn);

		JLabel lbl_1 = new JLabel("1. csvファイルを開く");
		lbl_1.setBounds(12, 10, 230, 27);
		frame.getContentPane().add(lbl_1);

		//テーブルの設定
		//カラムの名前
		String[] columnNames = {"OP", "COUNT", "AGE" };
		//テーブルモデル
		tableModel = new DefaultTableModel(null, columnNames);
		table = new JTable( tableModel );
		//table.setBounds(12, 181, 230, 250);
		//frame.getContentPane().add(table);
		JScrollPane tableArea = new JScrollPane(table);
		tableArea.setBounds(12, 144, 230, 250);
		frame.getContentPane().add(tableArea);

		JLabel lbl_3 = new JLabel("3. オペレータの年齢を入力（任意）");
		lbl_3.setBounds(12, 119, 230, 26);
		frame.getContentPane().add(lbl_3);

		JButton btnBtnselect = new JButton("btnSelect");
		btnBtnselect.setAction(actGetSelectedColumn);
		btnBtnselect.setBounds(178, 90, 64, 30);
		frame.getContentPane().add(btnBtnselect);

		JButton btnCalcOps = new JButton("");
		btnCalcOps.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnCalcOps.setAction(actCalcOps);
		btnCalcOps.setBounds(12, 404, 230, 40);
		frame.getContentPane().add(btnCalcOps);

		JLabel lbl_allQso = new JLabel("全QSO数");
		lbl_allQso.setBounds(12, 454, 134, 26);
		frame.getContentPane().add(lbl_allQso);

		JLabel lbl_u20Qso = new JLabel("20歳以下のOPのQSO");
		lbl_u20Qso.setBounds(12, 490, 134, 26);
		frame.getContentPane().add(lbl_u20Qso);

		JLabel lbl_u19Qso = new JLabel("未成年のOPのQSO");
		lbl_u19Qso.setBounds(12, 526, 134, 26);
		frame.getContentPane().add(lbl_u19Qso);

		txt_allQso = new JTextField();
		txt_allQso.setEditable(false);
		txt_allQso.setBounds(146, 454, 96, 26);
		frame.getContentPane().add(txt_allQso);
		txt_allQso.setColumns(10);

		txt_u20Qso = new JTextField();
		txt_u20Qso.setEditable(false);
		txt_u20Qso.setColumns(10);
		txt_u20Qso.setBounds(146, 490, 96, 26);
		frame.getContentPane().add(txt_u20Qso);

		txt_u19Qso = new JTextField();
		txt_u19Qso.setEditable(false);
		txt_u19Qso.setColumns(10);
		txt_u19Qso.setBounds(146, 526, 96, 26);
		frame.getContentPane().add(txt_u19Qso);
	}


	private class csvOpen extends AbstractAction {
		public csvOpen() {
			putValue(NAME, "Open CSV File");
			putValue(SHORT_DESCRIPTION, "ロギングソフトで出力したCSVファイルを開きます");
		}
		public void actionPerformed(ActionEvent e) {
			JFileChooser filechooser = new JFileChooser();
			FileFilter filter = new FileNameExtensionFilter( "カンマ区切り(.csv)", "csv" );
			filechooser.addChoosableFileFilter( filter );
			//int selected = filechooser.showOpenDialog(frame);
			if( filechooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION ){
				File file = filechooser.getSelectedFile();
				fileName = file.getPath();
				try {
					//スピナー全消し
					conboBoxModel.removeAllElements();

					String[] firstLine = ioCsv.readFirstLine(fileName);
					for( int i = 0; firstLine.length > i; i++ ){
						conboBoxModel.addElement( firstLine[i] );
					}
					//System.out.println(firstLine[0]);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "CSVファイルは開けましたが，内容の読み取りに失敗しました．");
					e1.printStackTrace();
				}
			}
		}
	}
	private class getSelectedColumn extends AbstractAction {
		public getSelectedColumn() {
			putValue(NAME, "決定");
			putValue(SHORT_DESCRIPTION, "選択した列からオペレータを取得します．");
		}
		public void actionPerformed(ActionEvent e) {
			//テーブル全消し
			while( tableModel.getRowCount() != 0 ){
				tableModel.removeRow(0);
			}
			//System.out.println(conboBoxModel.getIndexOf(conboBoxModel.getSelectedItem()));
			try {
				String[] opColumn = ioCsv.readOperatorColumn( fileName, conboBoxModel.getIndexOf(conboBoxModel.getSelectedItem()) );
				//System.out.println(opColumn[0]);
				mkList(opColumn);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(null, "CSVファイルは開けましたが，内容の読み取りに失敗しました．");
				e1.printStackTrace();
			}
		}
	}

	private void mkList( String[] opColumn ){
		ArrayList<String> opList = new ArrayList<String>();
		ArrayList<Integer> opCount = new ArrayList<Integer>();

		//間違えてOP一覧以外を選択したときのエラーフラグ（最初は-1）
		int errorFlag = -1;
		for( int i = 0; i < opColumn.length; i++ ){
			//数が多過ぎたら確認
			if( ( opList.size() > 30 ) && ( errorFlag == -1 )  ){
				errorFlag = JOptionPane.showConfirmDialog(null, "オペレータ数が30を超えます．本当にオペレータ一覧の列を選択しましたか？", "確認", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if( errorFlag != 0 ){
					break;
				}
			}
			//同じ名前がないか探す
			if( opList.indexOf(opColumn[i]) >= 0 ){
				//ある場合
				int index = opList.indexOf(opColumn[i]);
				opCount.set( index, opCount.get(index) + 1 );
			}else {
				//ない場合
				opList.add(opColumn[i]);
				opCount.add(1);
			}
			//opList[i][0] = opColumn[i];
		}
		if( ( errorFlag == 0 ) || ( errorFlag == -1 ) ){
			for( int i = 0; i < opList.size(); i++ ){
				String[] newRow = { opList.get(i), String.valueOf(opCount.get(i)) ,"" };
				tableModel.addRow( newRow );
			}
		}
	}

	private void mkList_old( String[] opColumn ){
		String[][] opList = new String[opColumn.length][3];
		int k = 0;
		for( int i = 0; i < opColumn.length; i++ ){
			//同じ名前がないか探す
			for( int j = 0; j <= opList.length; j++ ){
				if( j == opList.length ){
					//なかった場合
					opList[k][0] = opColumn[i];
					opList[k][1] = "1";
					k++;
				}else if( opColumn[i].equals(opList[j][0]) ){
					//あった場合
					opList[j][1] = String.valueOf(Integer.parseInt(opList[j][1]) + 1);
					break;
				}
			}
			//opList[i][0] = opColumn[i];
		}

		for( int i = 0; i < opList.length; i++ ){
			tableModel.addRow(opList[i]);
		}
	}
	private class calcOps extends AbstractAction {
		public calcOps() {
			putValue(NAME, "オペレータ割合計算");
			putValue(SHORT_DESCRIPTION, "未成年および20歳以下のOPの割合を計算します．OP全員の年齢を入力してから押して下さい．");
		}
		public void actionPerformed(ActionEvent e) {
			//System.out.println(tableModel.getValueAt(0, 2));
			int allOps = 0;
			int under20Ops = 0;//20歳以下
			int under19Ops = 0;//未成年
			boolean calcFlag = true;
			if( tableModel.getRowCount() != 0 ){
				for( int i = 0; i < tableModel.getRowCount(); i++ ){
					if( tableModel.getValueAt( i, 2 ) == "" ){
						JOptionPane.showMessageDialog(null, String.valueOf(i+1) + "番目のオペレータ「" + tableModel.getValueAt( i, 0 ) + "」の年齢が入力されていません．" );
						calcFlag = false;
						break;
					}
					int opAge = Integer.parseInt( tableModel.getValueAt( i, 2 ).toString() );
					int opQso = Integer.parseInt( tableModel.getValueAt( i, 1 ).toString() );
					allOps = allOps + Integer.parseInt( tableModel.getValueAt( i, 1 ).toString() );
					if( opAge <= 19 ){
						under19Ops = under19Ops + opQso;
						under20Ops = under20Ops + opQso;
					}else if( opAge <= 20 ){
						under20Ops = under20Ops + opQso;
					}
				}
				if( calcFlag ){
					txt_allQso.setText(String.valueOf(allOps));
					txt_u20Qso.setText(String.valueOf(under20Ops) + "(" + String.valueOf( Math.round(((float)under20Ops/(float)allOps*10000.0) ) / 100.0 ) + "%)");
					txt_u19Qso.setText(String.valueOf(under19Ops) + "(" + String.valueOf( Math.round(((float)under19Ops/(float)allOps*10000.0) ) / 100.0 ) + "%)");
				}
			}else{
				JOptionPane.showMessageDialog(null, "オペレータ一覧が空です" );
			}
			//System.out.println(allOps);
			//System.out.println(under19Ops);
			//System.out.println(under20Ops);

		}
	}
}
