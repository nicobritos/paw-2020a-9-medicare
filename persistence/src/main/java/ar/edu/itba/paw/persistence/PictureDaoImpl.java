package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.PictureDao;
import ar.edu.itba.paw.models.Picture;
import ar.edu.itba.paw.persistence.generics.GenericDaoImpl;
import ar.edu.itba.paw.persistence.utils.JDBCArgumentValue;
import ar.edu.itba.paw.persistence.utils.RowMapperAlias;
import ar.edu.itba.paw.persistence.utils.builder.JDBCSelectQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Repository
public class PictureDaoImpl extends GenericDaoImpl<Picture, Integer> implements PictureDao {
    public static final RowMapperAlias<Picture> ROW_MAPPER = (prefix, resultSet) -> {
        Picture picture = new Picture();
        try {
            picture.setId(resultSet.getInt(formatColumnFromName(PictureDaoImpl.PRIMARY_KEY_NAME, prefix)));
        } catch (SQLException e) {
            picture.setId(resultSet.getInt(PictureDaoImpl.PRIMARY_KEY_NAME));
        }
        populateEntity(picture, resultSet, prefix);
        return picture;
    };
    public static final String TABLE_NAME = getTableNameFromModel(Picture.class);
    public static final String PRIMARY_KEY_NAME = getPrimaryKeyNameFromModel(Picture.class);

    @Autowired
    public PictureDaoImpl(DataSource dataSource) {
        super(dataSource, Picture.class);
    }

    @Override
    protected ResultSetExtractor<List<Picture>> getResultSetExtractor() {
        return resultSet -> {
            Map<Integer, Picture> entityMap = new HashMap<>();
            List<Picture> sortedEntities = new LinkedList<>();
            while (resultSet.next()) {
                int id;
                try {
                    id = resultSet.getInt(this.formatColumnFromAlias(this.getIdColumnName()));
                } catch (SQLException e) {
                    id = resultSet.getInt(this.getIdColumnName());
                }
                if (resultSet.wasNull())
                    continue;
                entityMap.computeIfAbsent(id, string -> {
                    try {
                        Picture newEntity = ROW_MAPPER.mapRow(this.getTableAlias(), resultSet);
                        sortedEntities.add(newEntity);
                        return newEntity;
                    } catch (SQLException e) {
                        return null;
                    }
                });
            }
            return sortedEntities;
        };
    }

    @Override
    protected void populateJoins(JDBCSelectQueryBuilder selectQueryBuilder) {

    }

    @Override
    protected Map<String, JDBCArgumentValue> getModelRelationsArgumentValue(Picture model, String prefix) {
        return null;
    }
}
