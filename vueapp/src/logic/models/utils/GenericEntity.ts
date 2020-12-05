import {JSONSerializable, JSONSerializableKeys} from './JSONSerializable';
import {ID} from '~/logic/models/utils/Utils';

interface GenericEntityID {
    id: ID;
}

export abstract class GenericEntity<T = {}> implements GenericEntityID, JSONSerializable<T> {
    abstract id: ID;

    abstract toJSON(): JSONSerializableKeys<T>;
}
