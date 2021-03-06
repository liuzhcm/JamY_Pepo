package com.other_view;

import javax.swing.*;

import com.domain.TbBuyer;
import com.domain.TbClient;
import com.domain.TbGood;
import com.domain.TbInStorage;
import com.domain.TbKc;
import com.domain.TbOutStorage;
import com.domain.TbPicker;
import com.domain.TbStorage;
import com.domain.User;
import com.service.ClientService;
import com.service.GoodService;
import com.service.InStorageService;
import com.service.OutStorageService;
import com.service.OutStorageService.Item;
import com.service.PickerService;
import com.service.StorageService;
import com.util.JDateChooser;

import java.awt.*;

import javax.swing.JComboBox;

import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class OutStorageDialog extends JDialog implements ActionListener,MouseListener{
	
	//定义我需要的swing组件
	JLabel jl1,jl2,jl3,jl4,jl5,jl6,jl7;
	JButton jb1,jb2,jb3;
	JTextField jtf1,jtf2,jtf3,jtf4,jtf5,jtf6;
	JPanel jp1,jp2,jp3;
	JComboBox jcb1,jcb2,jcb3;
	
	OutStorageService outStorageService;
	String ono;//记录哪一条记录
	
	//owner他的父窗口
	//title窗口名
	//modal指定是模式窗口，还是非模式窗口
	public OutStorageDialog(JFrame owner,String title,boolean modal,OutStorageService outStorageService,int rowNums,String type)
	{
//		super(owner,title,modal);//调用父类构造方法，达到模式对话框效果 会导致居中失败
		jl1=new JLabel("货物名称");
		jl2=new JLabel("提货员");
		jl3=new JLabel("发货仓库");
		jl4=new JLabel("发往地");
		jl5=new JLabel("发货日期");
		jl6=new JLabel("发货数量");	
		
		jcb1=new JComboBox(new OutStorageService().getAllName("good"));
		jcb2=new JComboBox(new OutStorageService().getAllName("picker"));
		jcb3=new JComboBox(new OutStorageService().getAllName("storage"));
		jtf1=new JTextField();
		jtf2=new JTextField();	
		jtf2.addMouseListener(this);
		jtf3=new JTextField();
		
		jb1=new JButton("添加");
		if(type.equals("update")){
			jb3=new JButton("修改");
			jb3.addActionListener(this);
			ono=outStorageService.getValueAt(rowNums, 0).toString();
			jtf1.setText(outStorageService.getValueAt(rowNums, 4).toString());
			jtf2.setText(outStorageService.getValueAt(rowNums, 5).toString());
			jtf3.setText(outStorageService.getValueAt(rowNums, 6).toString());
			
			//这里需要改进
			jcb1.setSelectedIndex(0);//string数组根据值找到下标
			jcb2.setSelectedIndex(0);
			jcb3.setSelectedIndex(0);

			
		}
		jb1.addActionListener(this);
		jb2=new JButton("取消");
		jb2.addActionListener(this);
		
		jp1=new JPanel();
		jp2=new JPanel();
		jp3=new JPanel();
		
		//设置布局
		jp1.setLayout(new GridLayout(6,1));
		jp2.setLayout(new GridLayout(6,1));
		
		jp1.add(jl1);
		jp1.add(jl2);
		jp1.add(jl3);
		jp1.add(jl4);
		jp1.add(jl5);
		jp1.add(jl6);
		
		jp2.add(jcb1);
		jp2.add(jcb2);
		jp2.add(jcb3);
		jp2.add(jtf1);
		jp2.add(jtf2);
		jp2.add(jtf3);
		
		if(type.equals("add")){
			jp3.add(jb1);
		}else if(type.equals("update")){
			jp3.add(jb3);
		}
		jp3.add(jb2);
		
		this.add(jp1,BorderLayout.WEST);
		this.add(jp2,BorderLayout.CENTER);
		this.add(jp3,BorderLayout.SOUTH);
		
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setSize(300,250);
		this.setLocationRelativeTo(null);
		this.setTitle(title);
		this.setResizable(false);
		// 显示为模态对话框
		this.setModal(modal);
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		//实例化
		outStorageService = new OutStorageService();
		// TODO 自动生成的方法存根
		if(arg0.getSource()==jb1){
			//希望添加
			TbOutStorage o=new TbOutStorage();
			TbGood g=(TbGood)(((Item)(jcb1.getSelectedItem())).getObject());
			TbPicker p=(TbPicker)(((Item)(jcb2.getSelectedItem())).getObject());
			TbStorage s=(TbStorage)(((Item)(jcb3.getSelectedItem())).getObject());
			String address=jtf1.getText();
			String date=jtf2.getText();
			int num=Integer.parseInt(jtf3.getText());
			o.setTbGood(g);
			o.setTbPicker(p);
			o.setTbStorage(s);
			o.setOaddress(address);
			o.setOdate(date);
			o.setOnum(num);
			if(outStorageService.addOutStorage(o)){
				//this=从这个框里面出来
				JOptionPane.showMessageDialog(this, "添加成功");
			}else{
				JOptionPane.showMessageDialog(this, "添加失败");
				return;
			}
			//关闭对话
			this.dispose();
			
			//更新库存
			TbKc kc=new TbKc();
			kc.setTbGood(g);
			kc.setTbStorage(s);
			kc.setKnum(num);
			kc.setKdate(date);
			
			if(outStorageService.updateKc(kc)){
				//this=从这个框里面出来
				JOptionPane.showMessageDialog(this, "库存已更新");
			}else{
				JOptionPane.showMessageDialog(this, "库存更新失败");
				return;
			}
		}else if(arg0.getSource()==jb2){
			this.dispose();
		}else if(arg0.getSource()==jb3){
			//希望修改
			TbOutStorage o = new TbOutStorage();
			o.setOno(Integer.parseInt(this.ono));
			o.setTbGood((TbGood)(((Item)(jcb1.getSelectedItem())).getObject()));
			o.setTbPicker((TbPicker)(((Item)(jcb2.getSelectedItem())).getObject()));
			o.setTbStorage((TbStorage)(((Item)(jcb3.getSelectedItem())).getObject()));
			o.setOaddress(jtf1.getText());
			o.setOdate(jtf2.getText());
			o.setOnum(Integer.parseInt(jtf3.getText()));
			if(outStorageService.updOutStorage(o)){
				//this=从这个框里面出来
				JOptionPane.showMessageDialog(this, "修改成功 ");
			}else{
				JOptionPane.showMessageDialog(this, "添加失败");
				return;
			}
			//关闭对话
			this.dispose();
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getSource()==jtf2){
			JDateChooser gg = new JDateChooser();
			gg.showDateChooser();
			jtf2.setText(gg.getDateFormat("yyyy-MM-dd"));
		}	
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO 自动生成的方法存根
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO 自动生成的方法存根
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO 自动生成的方法存根
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO 自动生成的方法存根
		
	}

}