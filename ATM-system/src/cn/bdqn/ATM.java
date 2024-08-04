package cn.bdqn;

import com.sun.security.jgss.GSSUtil;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public  class  ATM {
    private ArrayList<Account> accounts = new ArrayList<>();
    private Scanner sc = new Scanner(System.in);
    private Account loginAcc;//记住登录后的用户账户

    /**
     * 启动ATM启动，展示欢迎界面
     */
    public void start() {
        while (true) {
            System.out.println("==欢迎您进入到了ATM系统==");
            System.out.println("1、用户登录");
            System.out.println("2、用户开户");
            System.out.println("请选择：");
            String command = sc.next();
            switch (command) {
                case "1":
                    //用户登录
                    login();
                    break;
                case "2":
                    //用户开户
                    createAccount();
                    break;
                default:
                    System.out.println("没有该操作~~");
            }
        }
    }


    /**
     * 完成用户的登录操作
     */

    private void login() {
        if (accounts.size() == 0) {
            System.out.println("当前系统中，还没有账户哦~~");
            return;
        }

        while (true) {
            System.out.println("请输入您的卡号：");
            String cardId = sc.next();

            Account acc = getAccountByCardId(cardId);
            if (acc == null) {
                System.out.println("您输入的卡号不存在,请确认~~");
            } else {
                while (true) {
                    System.out.println("请输入您的密码：");
                    String password = sc.next();
                    if (acc.getPassword().equals(password)) {

                        loginAcc = acc;

                        System.out.println("恭喜您，" + acc.getUserName() + "登录成功，您的卡号是：" + acc.getCardId());
                        showUserCommand();
                        return;
                    } else {
                        System.out.println("您的密码错误，请确认~~");
                    }
                }
            }
        }
    }

    /**
     * 展示登录后操作页面
     */
    private void showUserCommand() {
        while (true) {
            System.out.println("====" + loginAcc.getUserName() + "您可以选择如下功能进行账号的处理=====");
            System.out.println("1、查询账户");
            System.out.println("2、存款");
            System.out.println("3、取款");
            System.out.println("4、转账");
            System.out.println("5、密码修改");
            System.out.println("6、退出");
            System.out.println("7、注销当前账户");
            System.out.println("请选择：");
            String command = sc.next();
            switch (command) {
                case "1":
                    //查询账号
                    showLoginAccount();
                    break;
                case "2":
                    //存款
                    depositMoney();
                    break;
                case "3":
                    //取款
                    drawMoney();
                    break;
                case "4":
                    //转账
                    transferMoney();
                    break;
                case "5":
                    //密码修改
                    updatePassword();
                    return;
                case "6":
                    //退出
                    System.out.println(loginAcc.getUserName() + "您退出系统成功！");
                    return;
                case "7":
                    //注销当前登录的账户
                    if (deleteAccount()) {
                        return;
                    }
                    break;
                default:
                    System.out.println("没有该操作，请您确认~~");
            }
        }
    }

    /**
     * 密码修改
     */
    private void updatePassword() {
        System.out.println("==密码修改操作==");
        while (true) {
            System.out.println("请输入您当前账户的密码：");
            String password = sc.next();

            if (loginAcc.getPassword().equals(password)) {
                while (true) {
                    System.out.println("请您输入新密码：");
                    String newPassword = sc.next();
                    System.out.println("请您输入确认密码：");
                    String okPassword = sc.next();

                    if (okPassword.equals(newPassword)) {
                        loginAcc.setPassword(okPassword);
                        System.out.println("恭喜您，您的密码修改完成~~");
                        return;
                    } else {
                        System.out.println("您输入的两次密码不一致~~");
                    }
                }

            } else {
                System.out.println("您输入的密码不正确~~");
            }
        }
    }

    /**
     * 销户
     */
    private boolean deleteAccount() {
        System.out.println("==销户操作==");
        while (true) {
            System.out.println("您确定要销户吗，Y(确定) / N(不确定)");
            String command = sc.next();
            if (command.equalsIgnoreCase("Y")) {
                if (loginAcc.getMoney() == 0) {
                    accounts.remove(loginAcc);
                    System.out.println("您好，您的账户已经成功销户~~");
                    return true;
                } else {
                    System.out.println("对不起，您的账户中有余额，不允许销户~~");
                    return false;
                }
            } else {
                System.out.println("好的，保留您的账户~~");
                return false;
            }
        }

    }

    /**
     * 转账
     */
    private void transferMoney() {
        System.out.println("==用户转账==");

        if (accounts.size() < 2) {
            System.out.println("当前系统中只有一个账户，无法为其他用户转账~");
            return;
        }

        if (loginAcc.getMoney() == 0) {
            System.out.println("您自己都没有钱，就别给别人转了");
            return;
        }

        while (true) {
            System.out.println("请输入对方的卡号：");
            String cardId = sc.next();
            Account acc = getAccountByCardId(cardId);
            if (acc == null) {
                System.out.println("当前卡号不存在，请确认~~");
            } else {
                String name = "*" + acc.getUserName().substring(1);
                System.out.println("请输入对方【" + name + "】姓氏：");
                String peName = sc.next();
                if (acc.getUserName().startsWith(peName)) {
                    while (true) {
                        System.out.println("请输入给对方转账的金额：");
                        double money = sc.nextDouble();
                        if (loginAcc.getMoney() >= money) {
                            //更新自己账户的金额
                            loginAcc.setMoney(loginAcc.getMoney() - money);
                            //更新自己对方账户的金额
                            acc.setMoney(acc.getMoney() + money);
                            System.out.println("转账成功！您当前余额还剩余：" + loginAcc.getMoney());
                            return;
                        } else {
                            System.out.println("您的余额不足，最多可转：" + loginAcc.getMoney() + "元。");
                        }
                    }
                } else {
                    System.out.println("对不起，您认证的姓氏有问题~~~");
                }
            }
        }


    }

    /**
     * 取款
     */
    private void drawMoney() {
        System.out.println("==取款操作==");

        if (loginAcc.getMoney() < 100) {
            System.out.println("您的账户余额不足100元~~，不允许取钱~~");
            return;
        }
        while (true) {
            System.out.println("请输入您要取款的金额：");
            double money = sc.nextDouble();

            if (loginAcc.getMoney() >= money) {
                if (money > loginAcc.getLimit()) {
                    System.out.println("您当前取款金额超过了每次限额，您每次最多可取：" + loginAcc.getLimit());
                } else {
                    loginAcc.setMoney(loginAcc.getMoney() - money);
                    System.out.println("您本次取款金额：" + money + "取款后您剩余：" + loginAcc.getMoney() + "余额。");
                    break;
                }
            } else {
                System.out.println("余额不足，您的账户余额是：" + loginAcc.getMoney());
                System.out.println("是否还要继续取款呢？[Y(继续) / N(不继续)]：");
                String command = sc.next();
                if (command.equalsIgnoreCase("y")) {
                    continue;
                } else {
                    break;
                }
            }
        }
    }

    /**
     * 存款
     */
    private void depositMoney() {
        System.out.println("==存款操作==");
        System.out.println("请输入存款的金额：");
        double money = sc.nextDouble();

        loginAcc.setMoney(loginAcc.getMoney() + money);
        System.out.println("恭喜您，您存钱：" + money + "成功，存钱后余额是：" + loginAcc.getMoney());
    }

    /**
     * 展示当前登录的账户信息
     */

    private void showLoginAccount() {
        System.out.println("--------当前您的账户信息如下-------");
        System.out.println("户主：" + loginAcc.getUserName());
        System.out.println("卡号：" + loginAcc.getCardId());
        System.out.println("性别：" + loginAcc.getGender());
        System.out.println("余额：" + loginAcc.getMoney());
        System.out.println("每次取现额度：" + loginAcc.getLimit());
        System.out.println("-------------------------------");
    }


    /**
     * 完成用户开户操作
     */
    private void createAccount() {
        System.out.println("==系统开户操作==");
        Account acc = new Account();

        System.out.println("请输入您的账户名称：");
        String name = sc.next();
        acc.setUserName(name);

        while (true) {
            System.out.println("请输入您的性别：");
            char gender = sc.next().charAt(0);
            if (gender == '男' || gender == '女') {
                acc.setGender(gender);
                break;
            } else {
                System.out.println("性别只能输入男和女~~");
            }
        }

        while (true) {
            System.out.println("请输入您的密码：");
            String password = sc.next();
            System.out.println("请输入您的确认密码：");
            String okPassword = sc.next();

            if (okPassword.equals(password)) {
                acc.setPassword(password);
                break;
            } else {
                System.out.println("您输入的2次密码不一致，请您确认~~");
            }
        }

        System.out.println("请您输入取现额度：");
        double limit = sc.nextDouble();
        acc.setLimit(limit);


        String newCardId = createCardId();
        acc.setCardId(newCardId);

        accounts.add(acc);
        System.out.println("恭喜您，" + acc.getUserName() + "开户成功，您的卡号是：" + newCardId);
    }

    /**
     * 返回一个8位数字的卡号，且卡号不能重复
     */
    private String createCardId() {
        while (true) {
            String cardId = "";
            Random r = new Random();
            for (int i = 0; i < 8; i++) {
                cardId += r.nextInt(10);
            }
            Account acc = getAccountByCardId(cardId);
            if (acc == null) {
                return cardId;
            }
        }
    }

    /**
     * 根据卡号查询账户对象，没有返回null
     */
    private Account getAccountByCardId(String cardId) {
        for (int i = 0; i < accounts.size(); i++) {
                Account acc = accounts.get(i);
                if (acc.getCardId().equals(cardId)) {
                    return acc;
            }
        }
        return null;//卡号不存在
    }

}
