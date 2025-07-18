package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime; // 引入 LocalDateTime 處理時間戳
import jakarta.persistence.Transient;

@Entity
@Table(name = "themes")
public class Theme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String themeName;

    @Column(nullable = false)
    private String displayName;

    @Column(nullable = false)
    private Integer price;

    // 新增 created_at 欄位，對應資料庫的 timestamp 類型
    // @CreationTimestamp (來自 org.hibernate.annotations) 如果您使用 Hibernate 並且希望 JPA 自動設定此欄位
    @Column(name = "created_at", nullable = false, updatable = false) // updatable = false 表示創建後不可更新
    private LocalDateTime createdAt; // 使用 LocalDateTime 對應 timestamp 類型

    @Lob // 可選，表示是一個大的物件，JPA 會考慮更長的類型
    @Column(nullable = false, columnDefinition = "LONGBLOB")
    private byte[] page; // 將變數名改為 'page' 以更直接對應資料庫欄位

    // 無參數建構子 (JPA 需要)
    public Theme() {
    }
    @Transient // 告訴 JPA 這個欄位不需要儲存到資料庫
    private String base64PageDataUrl;

    // 全參數建構子
    public Theme(Long id, String themeName, String displayName, Integer price, LocalDateTime createdAt, byte[] page) {
        this.id = id;
        this.themeName = themeName;
        this.displayName = displayName;
        this.price = price;
        this.createdAt = createdAt;
        this.page = page;
    }

    // Getter 和 Setter 方法

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public byte[] getPage() {
        return page;
    }

    public void setPage(byte[] page) {
        this.page = page;
    }

	public String getBase64PageDataUrl() {
		return base64PageDataUrl;
	}

	public void setBase64PageDataUrl(String base64PageDataUrl) {
		this.base64PageDataUrl = base64PageDataUrl;
	}
    
}