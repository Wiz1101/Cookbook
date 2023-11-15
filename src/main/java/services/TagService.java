package services;

import java.util.List;
import java.util.UUID;

import models.entities.Tag;

public interface TagService {
    public List<Tag> getAllTags(UUID userId);
    public Tag getTagById(UUID tagId);
    public List<Tag> getTagsByRecipeId(UUID userId, UUID recipeId);
    public List<Tag> getTagsWithNameLike(UUID userId, String name);
    public String addTag(UUID tagId, String name, UUID userId);
    public List<UUID> getTagIdsByRecipeId(UUID userId, UUID recipeId);
    public Tag getTagByName(UUID userId, String name);
}
