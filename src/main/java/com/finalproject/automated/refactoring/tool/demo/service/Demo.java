package com.finalproject.automated.refactoring.tool.demo.service;

import org.springframework.lang.NonNull;

import java.util.List;

public interface Demo {

    void doCodeSmellDetection(@NonNull List<String> paths);
}
