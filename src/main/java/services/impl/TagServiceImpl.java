package services.impl;

import java.util.List;
import java.util.UUID;

import models.entities.Tag;
import services.TagService;
import util.common.DbContext;
import util.constants.Variables;

public class TagServiceImpl implements TagService {
    private DbContext dbContext;

    public TagServiceImpl() {
        super();
        dbContext = new DbContext(Variables.DATABASE_PORT, Variables.DATABASE_USER, Variables.DATABASE_PASS);
    }

    @Override
    public String addTag(UUID tagId, String name, UUID userId) {
        return dbContext.addTag(tagId, name, userId);
    }

    @Override
    public List<Tag> getAllTags(UUID userId) {
        return dbContext.getAllTags(userId);
    }

    @Override
    public List<Tag> getTagsByRecipeId(UUID userId, UUID recipeId) {
        return dbContext.getTagsByRecipeId(userId, recipeId);
    }

    @Override
    public List<Tag> getTagsWithNameLike(UUID userId, String name) {
        return dbContext.getTagsWithNameLike(userId, name);
    }

    @Override
    public Tag getTagById(UUID tagId) {
        return dbContext.getTagById(tagId);
    }

    @Override
    public List<UUID> getTagIdsByRecipeId(UUID userId, UUID recipeId) {
        return dbContext.getTagIdsByUserIdAndRecipeId(userId, recipeId);
    }

    @Override
    public Tag getTagByName(UUID userId, String name) {
        return dbContext.getTagByName(userId, name);
    }
}
