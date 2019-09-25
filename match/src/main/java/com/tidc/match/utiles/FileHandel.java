package com.tidc.match.utiles;

import com.tidc.match.Properties.DownProperties;
import com.tidc.match.mapper.MatchsMapper;
import com.tidc.match.ov.UserOV;
import com.tidc.match.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

/**
 * @Author: huangxiangdi
 * @Date: 2019/6/22 19:56
 * @Project SsmClassSystem
 * @extend：
 */

@Component
@Scope("prototype")
public class FileHandel {
    @Autowired
    ImageCheck imageCheck;
    @Autowired
    MatchsMapper matchsMapper;
    @Autowired
    ApplicationContextProvider ac;
    @Autowired
    private DownProperties downProperties;
    /**
     * 匹配文件类型
     * @param fileType  匹配格式： ".jsp,.ppt,.png"
     * @param fileName  文件名
     * @return boolean
     */
    public boolean isNumType(String fileType,String fileName){
        if (fileType!=null && fileName!=null){
            String substring = fileName.substring(fileName.indexOf("."));  //获取文件后缀名（判断类型）
            System.out.println("文件上传类型是："+substring);
            String[] str = fileType.split(",");                     //截取出来后缀名
            System.out.println(Arrays.toString(str));
            for (String s : str) {//遍历是否符合后缀
                if (substring.equals(s))  return true;
            }
            return false;
        }
        return false;
    }

    /**
     * 保存文件
     * @param path  存储得绝对路径
     * @param file  文件
     * @param fileName 自定义文件名字
     * @return  存储的绝对路径
     */
    public static String saveFile(String path, MultipartFile file, String fileName) throws IOException {
            //判断文件夹是否存在，动态创建
            File file1 = new File(path);
            if (file1.exists()) {
                file1.mkdirs();
            }
            //获取文件的名称
            String UploadName = file.getOriginalFilename();
            //获取文件后缀名
            String fileType = UploadName.substring(UploadName.indexOf("."));
            //创建文件名
            String UuidFilename = fileName+fileType;
            //完成文件上传
            file.transferTo(new File(path,UuidFilename));
            //是否成功存 入

        return new File(path+"\\"+UuidFilename).exists() ? UuidFilename : null;

    }

    /**
     *保存文件
     * @param relativePath  存储相对路径，将存在/WEB-INF/File/下
     * @param file  文件,自动生成一个唯一ID
     * @return 存储的绝对路径
     */
    public String saveFile(String relativePath, MultipartFile file){
        try {
            //项目绝对路径
            String path = this.getProjectAbsolutePath();
            path = path+"File\\"+relativePath;
            //判断文件夹是否存在，动态创建
            File file1 = new File(path);
            if (!file1.exists()) {
                file1.mkdirs();
            }
            //获取文件的名称
            String UploadName = file.getOriginalFilename();
            //获取文件后缀名
            String fileType = UploadName.substring(UploadName.indexOf("."));
            //生成唯一ID名
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            //创建文件名
            String UuidFilename = uuid+fileType;
            //完成文件上传
            file.transferTo(new File(path,UuidFilename));
            //是否成功存入
            return new File(path+UuidFilename).exists() ? path+UuidFilename : null;
        } catch (IOException e) {
           return null;
        }
    }

    /**
     * 获得当前WEB-INF的绝对路径
     * @return
     */
    public String getProjectAbsolutePath(){
        //file:/D:/JavaWeb/.metadata/.me_tcat/webapps/TestBeanUtils/WEB-INF/classes/
        String path=Thread.currentThread().getContextClassLoader().getResource("").toString();
        path=path.replace('/', '\\'); // 将/换成\
        path=path.replace("file:", ""); //去掉file:
        path=path.replace("classes\\", ""); //去掉class\
        path=path.substring(1); //去掉第一个\,如 \D:\JavaWeb...
        return path;
    }


    /**
     * @param resp
     * @param path         文件的绝对路径
     * @param downloadName 文件下载的名字  自定义名字
     */
    public void downloadFile(final HttpServletResponse resp, final String path, final String downloadName) throws Exception{
        String fileName = null;
        try {
            fileName = new String(downloadName.getBytes("GBK"), StandardCharsets.ISO_8859_1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        final File file = new File(path);
        resp.reset();
        resp.setContentType("application/octet-stream");
        resp.setCharacterEncoding("utf-8");
        resp.setContentLength((int) file.length());
        resp.setHeader("Content-Disposition", "attachment;filename=" +fileName);
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            os = resp.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(file));
            int i = 0;
            while ((i = bis.read(buff)) != -1) {
                os.write(buff, 0, i);
                os.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis!=null)
                    bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (os!=null)
                    os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public Matchs upMatch(MultipartFile[] files,Matchs matchs,Teacher teacher) throws IOException {
        for (MultipartFile f : files) {
            File dest = null;
            String filepath = null;
            if (!f.isEmpty()) {
//				文件名为比赛名字加上老师id+uuid
                String uuid = UUID.randomUUID().toString();
                uuid = uuid.replace("-", "");
                String fileName = matchs.getName() + teacher.getId()+uuid;
//                filepath = "D:\\7\\java\\match\\src\\main\\resources\\file\\";
                filepath = downProperties.getPhotoFile();
                String s = this.saveFile(filepath, f, fileName);
                dest = new File(s);
                if (imageCheck.isImage(dest)) {
                    matchs.setLogo(s);
                } else {
                    matchs.setUrl(s);
                }
            }
        }
        return matchs;
    }
    //老师创建项目或者修改项目的 1是创建2是修改为了不被重置报名人数 2是修改1是创建
    public Matchs putMatchs(Teacher teacher, Matchs matchs, MultipartFile[] files,int i) throws IOException {
        String school = teacher.getSchool();
        matchs.setSchool(school);
        if(i!=2){
            matchs.setNumber(0);
            matchs.setFlag(1);
        }

        matchs.setTeacher_id(teacher.getId());
        matchs = upMatch(files, matchs, teacher);
        return matchs;
    }
    public Works upWorks(MultipartFile[] files,Works works,Student student) throws IOException {
        for (MultipartFile f : files) {
            File dest = null;
            String filepath = null;
            if (!f.isEmpty()) {
//				文件名为比赛名字加上学生id+uuid
                String uuid = UUID.randomUUID().toString();
                uuid = uuid.replace("-", "");
                String fileName = works.getName() + student.getId()+uuid;
//                filepath = "D:\\7\\java\\match\\src\\main\\resources\\file\\";
                filepath = downProperties.getPhotoFile();
                String s = this.saveFile(filepath, f, fileName);
                dest = new File(s);
                if (imageCheck.isImage(dest)) {
                    works.setLogo(s);
                } else {
                    works.setUrl(s);
                }
            }
        }
        return works;
    }
    //学生创建比赛 1是创建2是修改
    public Works putWorks(Student student, Works works, MultipartFile[] files,int i) throws IOException {
        works.setWorks_student_id(student.getId());
        String url = works.getUrl();
        String logo = works.getLogo();
        boolean flag = false;
        works = upWorks(files,works,student);

        return works;
    }
    //删除文件 单个文件删除成功返回true，否则返回false
    public  boolean deleteFile(String fileName) {
        fileName = downProperties.getPhotoFile()+fileName;
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }
 public  String upfile(MultipartFile file,int id,String name) throws IOException {
     File dest = null;
     String filepath = null;
     if (!file.isEmpty()) {
//				文件名为比赛名字加上老师id+uuid
         String uuid = UUID.randomUUID().toString();
         uuid = uuid.replace("-", "");//去-
         String fileName = name + id + uuid;
         filepath = downProperties.getPhotoFile();
         String s = saveFile(filepath, file, fileName);
         return s;
     }
     return null;
 }
}
