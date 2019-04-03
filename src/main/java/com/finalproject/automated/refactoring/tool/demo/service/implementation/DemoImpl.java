package com.finalproject.automated.refactoring.tool.demo.service.implementation;

import com.finalproject.automated.refactoring.tool.classes.detection.service.ClassesDetection;
import com.finalproject.automated.refactoring.tool.demo.service.Demo;
import com.finalproject.automated.refactoring.tool.files.detection.model.FileModel;
import com.finalproject.automated.refactoring.tool.files.detection.service.FilesDetection;
import com.finalproject.automated.refactoring.tool.lazy.classes.detection.service.LazyClassDetection;
import com.finalproject.automated.refactoring.tool.model.ClassModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class DemoImpl implements Demo {

    @Autowired
    private FilesDetection filesDetection;

    @Autowired
    private ClassesDetection classesDetection;

    @Autowired
    private LazyClassDetection lazyClassDetection;

    private static final Long LAZY_CLASS_THRESHOLD = 5L;

    private Map<String, List<ClassModel>> lazyClasses = new LinkedHashMap<String, List<ClassModel>>();

    @Override
    public void doCodeSmellDetection(List<String> paths) {
        Map<String, List<FileModel>> files = filesDetection.detect(paths, ".java");
        files.forEach(this::doReadFiles);
    }

    private void doReadFiles(String path, List<FileModel> fileModels) {
        Map<String, List<ClassModel>> classes = classesDetection.detect(fileModels);

        System.out.println();
        System.out.println("Project Path -> " + path);
        System.out.println("Files detected -> " + fileModels.size());

//        fileModels.forEach((temp) ->{
//            System.out.println("Content for file :\n" + temp.getPath()+"\\"+temp.getFilename() + "\n");
//            System.out.println(temp.getContent());
//        });

        System.out.println("Class detected -> " + classes.size());

        classes.forEach(this::searchLazyClass);
        System.out.println("Lazy Class detected -> " + lazyClasses.size());
        System.out.println();
        lazyClasses.forEach(this::doPrintLazyClass);
//        classes.forEach(this::doPrintClass);
    }

    private void searchLazyClass(String filename, List<ClassModel> classModels) {
        List<ClassModel> lazyClasses = lazyClassDetection.detect(classModels, LAZY_CLASS_THRESHOLD);
        if(lazyClasses.size() > 0){
            this.lazyClasses.put(filename, lazyClasses);
        }
    }

    private void doPrintClass(String s, List<ClassModel> classModels) {
        classModels.forEach((temp) ->{
            System.out.println();
            System.out.println(s);
            System.out.println("Package : " + temp.getPackageName());
            System.out.println("Import : " + temp.getImports());
            System.out.println("Keywords : " + temp.getKeywords());
            System.out.println("ClassName : " + temp.getName());
            System.out.println("Extends : " + temp.getExtend());
            System.out.println("Implemets : " + temp.getImplement());
            System.out.println("Attributes : " + temp.getAttributes());
            System.out.print("Methods : [" + temp.getMethodModels().size() + " ");
            temp.getMethodModels().forEach((m) ->{
                System.out.print(m.getName() + " ");
            });
            System.out.println("]");
            System.out.println("NOM : " + temp.getNom());
            System.out.println("NOF : " + temp.getNof());
            System.out.println();
        });
    }

    private void doPrintLazyClass(String s, List<ClassModel> classModels) {
        for(ClassModel classModel : classModels){
            System.out.println("Lazy class detected -> " + classModel.getName());
            System.out.println("NOM : " + classModel.getNom());
            System.out.println("NOF : " + classModel.getNof());
        }
    }
}
